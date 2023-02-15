#include <Arduino.h>
#include "FS.h"
#include "SD.h"
#include "SPI.h"
#include "FileOperations.h"
#include "ConfigLoader.h"


void setup(){
  loadConfiguration();
}

void loop(){

}