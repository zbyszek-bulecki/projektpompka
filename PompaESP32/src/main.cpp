#include <Arduino.h>
#include <Sensors.h>

Sensors sensors(34,3000,1100,0x4A,0x76,0x20,7);
uint32_t interval = 5000;
uint32_t lastDisplay = 0;

void setup() {
  Serial.begin(9600);
  sensors.begin();
}

void loop() {
  if (millis() - lastDisplay >= interval)
  {
    lastDisplay += interval;
    Serial.print ("gleba: ");
    Serial.println(sensors.getValueSoilMoisture());
    Serial.print ("swiatlo: ");
    Serial.println(sensors.getLuxValueLightSensor());
    Serial.print ("temp: ");
    Serial.println(sensors.getTemperature());
    Serial.print ("cisnienie: ");
    Serial.println(sensors.getPressure());
    Serial.print ("poziom wody: ");
    Serial.println(sensors.getValueWaterLevel());
  }
  
}