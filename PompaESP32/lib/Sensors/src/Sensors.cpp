

#include "Sensors.h"

Sensors::Sensors()
{
}
Sensors::Sensors(int soilMoisturePin, int minValueSoilMoisture, int maxValueSoilMoisture, uint8_t lightSensorAddress, uint8_t tempSensorAddress, uint8_t waterLevelAddress, int numberWaterLevel)
{
    soil_Moisture_PIN = soilMoisturePin;
    setScropeSoilMoisture(minValueSoilMoisture, maxValueSoilMoisture);
    light_Sensor = new Max44009(lightSensorAddress);
    bmp = new Adafruit_BMP280;
    bmp_Address = tempSensorAddress;
    water_level = new Water_level(waterLevelAddress, numberWaterLevel);
}

Sensors::Sensors(int soilMoisturePin, uint8_t lightSensorAddress, uint8_t tempSensorAddress, uint8_t waterLevelAddress, int numberWaterLevel)
{
    soil_Moisture_PIN = soilMoisturePin;
    light_Sensor = new Max44009(lightSensorAddress);
    bmp = new Adafruit_BMP280;
    bmp_Address = tempSensorAddress;
    water_level = new Water_level(waterLevelAddress, numberWaterLevel);
}

Sensors::Sensors(int soilMoisturePin, uint8_t lightSensorAddress)
{
    soil_Moisture_PIN = soilMoisturePin;
    light_Sensor = new Max44009(lightSensorAddress);
}

Sensors::Sensors(uint8_t tempSensorAddress, uint8_t waterLevelAddress, int numberWaterLevel)
{
    bmp = new Adafruit_BMP280;
    bmp_Address = tempSensorAddress;
    water_level = new Water_level(waterLevelAddress, numberWaterLevel);
}

Sensors::~Sensors()
{
}

void Sensors::begin()
{
    if(water_level) 
    {
        water_level->begin();
    } 
    if(bmp) 
    {
        bmp->begin(bmp_Address);
        bmp->setSampling(Adafruit_BMP280::MODE_NORMAL,     /* Operating Mode. */
                         Adafruit_BMP280::SAMPLING_X2,     /* Temp. oversampling */
                         Adafruit_BMP280::SAMPLING_X16,    /* Pressure oversampling */
                         Adafruit_BMP280::FILTER_X16,      /* Filtering. */
                         Adafruit_BMP280::STANDBY_MS_500); /* Standby time. */  
    }

    if(lcd) 
    {
        lcd->init();
        lcd->backlight();
    }
}

void Sensors::setScropeSoilMoisture(int minValue, int maxValue)
{
    min_Value_Soil_Moisture = minValue;
    max_Value_Soil_Moisture = maxValue;
}

int Sensors::getValueSoilMoisture()
{
    if (soil_Moisture_PIN > 0) {
        value_Soil_Moisture = analogRead(soil_Moisture_PIN);
        return map(value_Soil_Moisture, min_Value_Soil_Moisture, max_Value_Soil_Moisture, 0, 100);
    }
    return -100;
}

float Sensors::getLuxValueLightSensor()
{
    if (light_Sensor)
    {
        if (light_Sensor->getError() != 0) 
        {
            return -100;
        }
        else
        {
            return light_Sensor->getLux();
        }
    }
    else
    {
        return -100;
    }
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
        return bmp->readPressure()/100;
    }
    return -100;
}

int Sensors::getValueWaterLevel()
{
    if(water_level)
    {
        return water_level->getWaterLevel();
    }
    return -100;
}