#include "newSensors.h"

SensorsPompka::SensorsPompka()
{
    Wire.begin(21, 22); // Inicjalizacja I²C: SDA=GPIO21, SCL=GPIO22
    Serial.begin(115200);

    Wire.beginTransmission(PCF8574_ADDRESS);
    Wire.write(0xFF); // Ustawienie wszystkich pinów jako wejścia (wejścia "podciągnięte" do 1)
    Wire.endTransmission();

    bmp = new Adafruit_BMP280;
    bmp->begin(TEMP_SENSOR_ADDRESS);
    bmp->setSampling(Adafruit_BMP280::MODE_NORMAL,     /* Operating Mode. */
                     Adafruit_BMP280::SAMPLING_X2,     /* Temp. oversampling */
                     Adafruit_BMP280::SAMPLING_X16,    /* Pressure oversampling */
                     Adafruit_BMP280::FILTER_X16,      /* Filtering. */
                     Adafruit_BMP280::STANDBY_MS_500); /* Standby time. */
}

Measurements SensorsPompka::getMeasurements()
{
    Serial.print("Poziom wody: ");
    Serial.println(readWaterLevel()); // Output 0 if no water, or 1-4 for levels

    Serial.print("Temperatura: ");
    Serial.println(getTemperature());

    Serial.print("Ciśnienie atmosferyczne: ");
    Serial.println(getPressure());

    Serial.print("Wilgotność gleby: ");
    Serial.println(readSoilMoisture());

    Serial.print("Ilość światła: ");
    Serial.println(readLightIntensity());

    Measurements measurements;
    measurements.waterLevel = readWaterLevel();
    measurements.temperature = getTemperature();
    measurements.pressure = getPressure();
    measurements.soilMoisture = readSoilMoisture();
    measurements.lightIntensity = readLightIntensity();

    Serial.print("Poziom wody: ");
    Serial.println(measurements.waterLevel); // Output 0 if no water, or 1-4 for levels

    Serial.print("Temperatura: ");
    Serial.println(measurements.temperature);

    Serial.print("Ciśnienie atmosferyczne: ");
    Serial.println(measurements.pressure);

    Serial.print("Wilgotność gleby: ");
    Serial.println(measurements.soilMoisture);

    Serial.print("Ilość światła: ");
    Serial.println(measurements.lightIntensity);

    return measurements;
}

float SensorsPompka::getTemperature()
{
    if (bmp)
    {
        return bmp->readTemperature();
    }
    return -100;
}

float SensorsPompka::getPressure()
{
    if (bmp)
    {
        return bmp->readPressure() / 100;
    }
    return -100;
}

int SensorsPompka::readWaterLevel()
{
    Wire.requestFrom(PCF8574_ADDRESS, 1); // Odczyt 1 bajtu z PCF8574
    if (Wire.available())
    {
        byte pinStates = Wire.read(); // Odczyt stanu wszystkich pinów (P0-P7)

        int waterLevel = 0; // Default to 0 if no water is detected

        for (int i = 0; i < 4; i++)
        { // Iterate through P0-P3
            if (bitRead(pinStates, i) == 0)
            {                       // Water detected (pin reads 0)
                waterLevel = i + 1; // Map P0-P3 to water levels 1-4
            }
            else
                break;
        }
        return waterLevel;
    }
    return -1; // return -1 if the sensor is broken
}

int SensorsPompka::readSoilMoisture()
{
    const int adcPin = 34;                     // Pin podłączenia czujnika pomiaru wilgotności gleby
    int adcValue = analogRead(adcPin);         // Odczyt surowej wartości ADC
    float voltage = adcValue * (3.3 / 4095.0); // Przekształcenie wartości ADC na napięcie (dla zakresu 0-3.3V)
    if (voltage > 2.1)
        return 0; // DRY
    else if (voltage > 1.8)
        return 1; // MOIST
    return 2;     // WET
}

float SensorsPompka::readLightIntensity()
{
    const int luxPin = 25;             // Pin podłączenia czujnika pomiaru wilgotności gleby
    int adcValue = analogRead(luxPin); // Odczyt surowej wartości ADC
    float voltage = adcValue * (3.3 / 4095.0);
    return voltage;
}
