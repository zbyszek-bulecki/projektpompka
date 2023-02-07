#include <Arduino.h>
#include <Sensors.h>
#include <ArduinoJson.h>
#include "WiFi.h"
#include "SharkFrameManager.h"
#include "FS.h"
#include "SD.h"
#include "SPI.h"
#include "FileOperations.h"

#define uS_TO_S_FACTOR 1000000
#define TIME_TO_SLEEP 5

const char *WIFI_CONFIG_PATH = "/config/wifi.txt";

Sensors sensors(34, 2700, 1000, 0x4A, 0x76, 0x20, 7);
uint32_t interval = 600000;
uint32_t lastDisplay = 0;

void sendSensorsValue()
{
  const uint16_t port = 1234;
  const char *host = "130.162.59.92";
  Serial.print("Connecting to ");
  Serial.println(host);

  WiFiClient client;
  if (!client.connect(host, port))
  {
    Serial.println("Connection failed.");
    Serial.println("Waiting 5 seconds before retrying...");
    delay(5000);
    return;
  }
  digitalWrite(LED_BUILTIN, HIGH);

  DynamicJsonDocument doc(1024);
  doc["address"] = "0"; // WiFi.macAddress();
  doc["soil"] = sensors.getValueSoilMoisture();
  doc["light"] = String(sensors.getLuxValueLightSensor(), 2);
  doc["temp"] = String(sensors.getTemperature(), 2);
  doc["pressure"] = String(sensors.getPressure(), 2);
  doc["water"] = sensors.getValueWaterLevel();

  float voltage = 0;
  for (int i = 0; i < 32; i++)
  {
    voltage += analogRead(32);
  }
  voltage /= 32;
  voltage = voltage * (3.3 / 4096);
  voltage = voltage * 2.1;
  doc["voltage"] = String(voltage, 2);

  SharkFrameManager manager = SharkFrameManager(client);
  String outJson;
  serializeJson(doc, outJson);
  Serial.println(outJson);
  manager.send("sensors", outJson);

  String data;
  serializeJson(doc, data);

  if (SD.exists("/logs/sensorData.txt"))
  {
    appendFile(SD, "/logs/sensorData.txt", data.c_str());
  }
  else
  {
    createDir(SD, "/logs");
    writeFile(SD, "/logs/sensorData.txt", data.c_str());
    Serial.println("Logs folder created.");
  }

  readFile(SD, "/logs/sensorData.txt");

  SharkMessage msg = manager.read();

  if (msg.success)
  {
    Serial.print("key: ");
    Serial.println(msg.key.c_str());
    Serial.print("value: ");
    Serial.println(msg.value.c_str());
  }

  Serial.println("Closing connection.");
  client.stop();
  digitalWrite(LED_BUILTIN, LOW);
}

void setup()
{

  if (!SD.begin())
  {
    Serial.println("Card Mount Failed");
    return;
  }

  std::string ssid;
  std::string wifiPassword;
  Serial.begin(115200);
  pinMode(LED_BUILTIN, OUTPUT);
  pinMode(32, INPUT);
  sensors.begin();

  if (!SD.exists(WIFI_CONFIG_PATH))
  {
    char wifiConfig[] = "ssid=password";
    createDir(SD, "/config");
    writeFile(SD, WIFI_CONFIG_PATH, wifiConfig);
    Serial.println("Blank WiFi config data created.");
  }
  else
  {

    File myfile = SD.open(WIFI_CONFIG_PATH);
    std::string wifiConfig;
    if (myfile)
    {
      while (myfile.available())
      {
        wifiConfig += (myfile.read());
      }
      myfile.close();
    }
    else
    {
      Serial.println("Error opening WiFi config file.");
    }
    Serial.println(wifiConfig.c_str());

    char *p;
    p = strchr(wifiConfig.c_str(), '=');
    wifiPassword.append(p + 1);
    Serial.println(wifiPassword.c_str());

    int b;
    b = (wifiConfig.length() - wifiPassword.length() - 1);
    for (int i = 0; i < b; i++)
    {
      ssid += wifiConfig[i];
    }
    Serial.println(ssid.c_str());
  }

  WiFi.begin(ssid.c_str(), wifiPassword.c_str());
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  sendSensorsValue();

  esp_sleep_enable_timer_wakeup(TIME_TO_SLEEP * uS_TO_S_FACTOR);
  Serial.println("ESP32 is going to sleep now ;-)");
  Serial.flush();
  esp_deep_sleep_start();
}

void loop()
{
  // Code in this loop is never executed.

  // if (millis() - lastDisplay >= interval)
  // {
  //   lastDisplay += interval;
  //   sendSensorsValue();
  // }
}