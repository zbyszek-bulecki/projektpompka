#include <Arduino.h>
#include "FS.h"
#include "SD.h"
#include "SPI.h"
#include "FileOperations.h"
#include "ConfigLoader.h"


void setup(){

  Serial.println("Hello from setup BEFORE the loadConfiguration function is activated.");
  // loadConfiguration();
  Serial.println("Hello from setup AFTER the loadConfiguration fuction is activated");
  
}

void loop(){

Serial.print('.');
delay(5000);

}