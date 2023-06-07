
#ifndef SENSORS_H
#define SENSORS_H

#include "Arduino.h"

#include <Water_level.h>
#include <SPI.h>             // BMP 280
#include <Adafruit_BMP280.h> // BMP 280

#include <LiquidCrystal_I2C.h> //LCD

#define PI 3.1415926535897932384626433832795

// 0x4A  GY-49 MAX44009
// 0x76  BMP280
// 0x20  water level
// 0x27  LCD 16X2

#define MAX44009_ADDRESS (0x4A)
#define BMP280_ADDRESS (0x77)
#define WATER_LEVEL_ADDRESS (0x20)
#define LCD_ADDRESS (0x27)

class Sensors
{
private:
    int light_Sensor_PIN;  // light sensor (PIN33)
    int soil_Moisture_PIN; // soil moisture sensor  (PIN 34)
    int value_Soil_Moisture = 0;
    int min_Value_Soil_Moisture = -1; // 4095;
    int max_Value_Soil_Moisture = -1;

    Adafruit_BMP280 *bmp = NULL; // temperature & pressure sensor
    uint8_t bmp_Address = 0;

    Water_level *water_level = NULL; // Water sensor (0x20, 7)
    float depth = 22.0;
    float diameter = 9.5;

    LiquidCrystal_I2C *lcd = NULL; // LCD 16x2 (0x27, 16, 2)

public:
    Sensors(
        int lightSensorPin,
        int soilMoisturePin,
        int minValueSoilMoisture,
        int maxValueSoilMoisture,
        uint8_t tempSensorAddress,
        uint8_t waterLevelAddress,
        int numberWaterLevel);

    void begin();

    void setScropeSoilMoisture(int minValue, int maxValue);

    int getValueSoilMoisture();

    int getLuxValueLightSensor();

    float getTemperature();

    float getPressure();

    int getValueWaterLevel();
};

#endif