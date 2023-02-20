#include <Arduino.h>
#include "FS.h"
#include "SD.h"
#include "SPI.h"
#include "ConfigLoader.h"


void setup(){

  const char *CONFIG_PATH = "/config/config.txt";

  Serial.begin(115200);
  delay(1000);
  Serial.println("Hello from setup BEFORE the loadConfiguration function is activated.");
  loadConfiguration(CONFIG_PATH);
  delay(1000);
  Serial.println("Hello from setup AFTER the loadConfiguration fuction is activated");
  
}

void loop(){

Serial.print('.');
delay(3000);

}