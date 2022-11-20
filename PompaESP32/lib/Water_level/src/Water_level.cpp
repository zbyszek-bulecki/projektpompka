

#include "Water_level.h"
// #include "PCF8574.h"


// PCF8574 pcf8574()

Water_level::Water_level(uint8_t address, int level) 
  : pcf8574_(address)
{
  address_ = address;
  level_ = level;

}

bool Water_level::begin() {
  pcf8574_.pinMode(P0, OUTPUT);
  for (int i = 0; i < 7; ++i) {
    pcf8574_.pinMode(pins[i], INPUT_PULLUP);
  }
  return pcf8574_.begin();
}

int Water_level::getLevel() {
  return level_;
}

int Water_level::getWaterLevel() {
  pcf8574_.digitalWrite(P0, HIGH);
  delay(500);
  int val1 = 0;
  for (int i = 0; i < level_; i++) {
    val1 = val1 + !pcf8574_.digitalRead(pins[i], true);
    delay(100);
  }
  pcf8574_.digitalWrite(P0, LOW);
  return val1;
}

int Water_level::getWaterLevelPrecent() {
  return map(Water_level::getWaterLevel(), 0, level_, 0, 100);
}
