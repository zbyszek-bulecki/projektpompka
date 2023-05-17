#include <Arduino.h>
#include <Sensors.h>
#include <ArduinoJson.h>
#include "WiFi.h"
#include "SharkFrameManager.h"

const char* ssid      = "Pancernik";
const char* password  = "KawkaKawusia@2k21";

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
    doc["address"] = WiFi.macAddress();
    doc["soil"] = sensors.getValueSoilMoisture();
    doc["light"] = sensors.getLuxValueLightSensor();
    doc["temp"] = sensors.getTemperature();
    doc["pressure"] = sensors.getPressure();
    doc["water"] = sensors.getValueWaterLevel();
    
    SharkFrameManager manager = SharkFrameManager(client);
    String outJson;
    serializeJson(doc, outJson);

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