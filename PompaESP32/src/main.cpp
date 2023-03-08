#include <ConfigLoader.h>
#include <Arduino.h>
#include <FS.h>
#include <SD.h>
#include <SPI.h>

struct esp32config
{
  char ssid[60];
  char wifPwd[60];
  char server_ip_address;
  char server_port[60];
  char sleepTime[60];
};

  const char *CONFIG_PATH = "/config/config.txt";

  void setup()
  {

    Serial.begin(115200);
    Serial.println("Hello from setup BEFORE the loadConfiguration function is activated.");
    struct esp32config myConfig;

    myConfig = parseConfiguration(CONFIG_PATH);

    Serial.println("=== STRUCT ===");
    Serial.println("*** SSID ***");
    Serial.println(myConfig.ssid);

    Serial.println("*** WiFi-PWD ***");
    Serial.println(myConfig.wifPwd);

    Serial.println("*** Server IP Address ***");
    Serial.println(myConfig.server_ip_address);

    Serial.println("*** Server Port ***");
    Serial.println(myConfig.server_port);

    Serial.println("*** Client Sleep Time ***");
    Serial.println(myConfig.sleepTime);
    Serial.println("=== END OF STRUCT ===");

    Serial.println("Hello from setup AFTER the loadConfiguration fuction is activated");
  }

  void loop()
  {
    Serial.print('.');
    delay(3000);
  }