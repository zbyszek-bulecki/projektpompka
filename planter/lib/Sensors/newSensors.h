#pragma once
#include <Wire.h>
#include <Arduino.h>

// Adres PCF8574 (domyślny: 0x20, zależnie od A0, A1, A2)
#define PCF8574_ADDRESS 0x20

void init();
void getMeasurements();
int readWaterLevel();
