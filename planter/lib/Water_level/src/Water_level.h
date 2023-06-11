
#ifndef WATER_LEVEL_H
#define WATER_LEVEL_H

#include "Arduino.h"
#include "PCF8574.h"

const uint8_t pins[] = {P1, P2, P3, P4, P5, P6, P7};

class Water_level
{
private:
    uint8_t address_;
    int level_;
    PCF8574 pcf8574_;
    
public:
    Water_level(uint8_t address, int level);
    bool begin();
    int getLevel();
    int getWaterLevel();
    int getWaterLevelPrecent();
};

#endif

