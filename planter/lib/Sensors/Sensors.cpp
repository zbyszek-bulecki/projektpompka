#include "Sensors.h"

Sensors::Sensors(
    int lightSensorPin,
    int soilMoisturePin,
    int minValueSoilMoisture,
    int maxValueSoilMoisture,
    uint8_t tempSensorAddress,
    uint8_t waterLevelAddress,
    int numberWaterLevel)
{
    this.minValueSoilMoisture = minValueSoilMoisture;
    this.maxValueSoilMoisture = maxValueSoilMoisture;
    soil_Moisture_PIN = soilMoisturePin;
    light_Sensor_PIN = lightSensorPin;
    bmp = new Adafruit_BMP280;
    bmp_Address = tempSensorAddress;
    water_level = new Water_level(waterLevelAddress, numberWaterLevel);
}

void Sensors::begin()
{
    // if (water_level)
    // {
    //     water_level->begin();
    // }
    if (bmp)
    {
        bmp->begin(bmp_Address);
        bmp->setSampling(Adafruit_BMP280::MODE_NORMAL,     /* Operating Mode. */
                         Adafruit_BMP280::SAMPLING_X2,     /* Temp. oversampling */
                         Adafruit_BMP280::SAMPLING_X16,    /* Pressure oversampling */
                         Adafruit_BMP280::FILTER_X16,      /* Filtering. */
                         Adafruit_BMP280::STANDBY_MS_500); /* Standby time. */
    }
}

int Sensors::getValueSoilMoisture()
{
    if (soil_Moisture_PIN > 0 && max_Value_Soil_Moisture > -1)
    {
        for (int i = 0; i < 64; i++)
        {
            value_Soil_Moisture += analogRead(soil_Moisture_PIN);
        }
        value_Soil_Moisture /= 64;
        value_Soil_Moisture = map(value_Soil_Moisture, min_Value_Soil_Moisture, max_Value_Soil_Moisture, 0, 100);
        // TODO remove max and min parameters once they are no longer needed
        if (value_Soil_Moisture > 100)
        {
            return 100;
        }
        else if (value_Soil_Moisture < 0)
        {
            return 0;
        }
        else
        {
            return value_Soil_Moisture;
        }
    }
    return -100;
}

int Sensors::getLuxValueLightSensor()
{
    short luxValue = analogRead(light_Sensor_PIN);
    return map(luxValue, 0, 1024, 0, 100);
}

float Sensors::getTemperature()
{
    if (bmp)
    {
        return bmp->readTemperature();
    }
    return -100;
}

float Sensors::getPressure()
{
    if (bmp)
    {
        return bmp->readPressure() / 100;
    }
    return -100;
}

int Sensors::getValueWaterLevel()
{
    if (water_level)
    {
        return water_level->getWaterLevel();
    }
    return -100;
}