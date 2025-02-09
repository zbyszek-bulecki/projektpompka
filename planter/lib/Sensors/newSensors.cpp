#include "newSensors.h"

void init()
{
    Wire.begin(21, 22); // Inicjalizacja I²C: SDA=GPIO21, SCL=GPIO22
    Serial.begin(115200);

    Wire.beginTransmission(PCF8574_ADDRESS);
    Wire.write(0xFF); // Ustawienie wszystkich pinów jako wejścia (wejścia "podciągnięte" do 1)
    Wire.endTransmission();
}

void getMeasurements()
{
    init();
    int waterLevel = readWaterLevel();
    Serial.print("Poziom wody: ");
    Serial.println(waterLevel); // Output 0 if no water, or 1-4 for levels
}

int readWaterLevel()
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
