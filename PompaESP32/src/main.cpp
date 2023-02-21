#include <Arduino.h>
#include <FS.h>
#include <SD.h>
#include <SPI.h>
#include <ConfigLoader.h>

const char *CONFIG_PATH = "/config/config.txt";

void setup(){

  Serial.begin(115200);
  delay(1000);
  Serial.println("Hello from setup BEFORE the loadConfiguration function is activated.");
  parseConfiguration(CONFIG_PATH);
  delay(1000);
  Serial.println("Hello from setup AFTER the loadConfiguration fuction is activated");
}

void loop(){
  Serial.print('.');
  delay(3000);
}