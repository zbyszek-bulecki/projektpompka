#pragma once
#include <Wire.h>
#include <Arduino.h>
#include <Adafruit_BMP280.h> // BMP 280

// Adres PCF8574 (domyślny: 0x20, zależnie od A0, A1, A2)
#define PCF8574_ADDRESS 0x20
#define TEMP_SENSOR_ADDRESS 0x76

struct Measurements
{
    int waterLevel;
    float temperature;
    float pressure;
    int soilMoisture;
    float lightIntensity;
};

class SensorsPompka
{
    Adafruit_BMP280 *bmp = NULL; // temperature & pressure sensor
    uint8_t bmp_Address = 0;

    int readWaterLevel();

    float getTemperature();
    float getPressure();
    int readSoilMoisture();
    float readLightIntensity();

public:
    SensorsPompka();
    Measurements getMeasurements();
};