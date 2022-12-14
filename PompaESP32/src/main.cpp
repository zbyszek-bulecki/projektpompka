#include <Arduino.h>
#include <Sensors.h>
#include <ArduinoJson.h>
#include "WiFi.h"
#include "SharkFrameManager.h"

const char* ssid      = "HUAWEI_B818_8EB9";
const char* password  = "R6AAQ903L9E";

Sensors sensors(34,2700,1000,0x4A,0x76,0x20,7);
uint32_t interval = 600000;
uint32_t lastDisplay = 0;

void sendSensorsValue() {
const uint16_t port = 1234; // port TCP server
    // const char * host = "192.168.0.206"; // ip or dns
    // const char * host = "192.168.8.107";
    const char * host = "130.162.59.92"; 
    Serial.print("Connecting to ");
    Serial.println(host);
    // Use WiFiClient class to create TCP connections
    WiFiClient client;
    if (!client.connect(host, port)) {
      Serial.println("Connection failed.");
      Serial.println("Waiting 5 seconds before retrying...");
      delay(5000);
      return;
    }
    digitalWrite(LED_BUILTIN, HIGH);

    DynamicJsonDocument doc(1024);
    doc["address"] = "0";//WiFi.macAddress();
    doc["soil"] = sensors.getValueSoilMoisture();
    doc["light"] = String(sensors.getLuxValueLightSensor(), 2);
    doc["temp"] = String(sensors.getTemperature(), 2);
    doc["pressure"] = String(sensors.getPressure(), 2);
    doc["water"] = sensors.getValueWaterLevel();

    float voltage = 0;
    for (int i = 0; i < 32; i++){
      voltage += analogRead(32);
    }
    voltage /=32;
    voltage = voltage * (3.5/4096);
    voltage = voltage * 2.0;
    doc["voltage"] = String(voltage, 2);


    SharkFrameManager manager = SharkFrameManager(client);
    String outJson;
    serializeJson(doc, outJson);

Serial.println(outJson);

    manager.send("sensors", outJson);

    SharkMessage msg = manager.read();
    if(msg.success){
      Serial.print("key: ");
      Serial.println(msg.key.c_str());
      Serial.print("value: ");
      Serial.println(msg.value.c_str());
    }

    Serial.println("Closing connection.");
    client.stop();
    digitalWrite(LED_BUILTIN, LOW);
}

void setup() {
  Serial.begin(9600);
  pinMode(LED_BUILTIN, OUTPUT);
  pinMode(32, INPUT); 
  sensors.begin();

  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
    Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());

    sendSensorsValue();
}

void loop() {
  
  if (millis() - lastDisplay >= interval)
  {
    lastDisplay += interval;
    sendSensorsValue();
  }

}