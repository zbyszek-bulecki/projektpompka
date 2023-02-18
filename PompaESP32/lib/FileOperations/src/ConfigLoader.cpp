#include "FS.h"
#include "SD.h"
#include "SPI.h"

const char *WIFI_CONFIG_PATH = "/config/config.txt";
std::string wifiConfig;

    struct
  {
    std::string ssid;
    std::string wifPwd;
    uint16_t server_ip_address;
    uint16_t server_port;
    uint16_t sleepTime;
  } esp32config;

void loadConfiguration()
{
      if (!SD.begin())
  {
    Serial.println("Card Mount Failed");
    return;
  }
    File myfile = SD.open(WIFI_CONFIG_PATH);
    std::string wifiConfig;
    std::string tmpString;

    if (myfile)
    {
        while (myfile.available())
        {
            wifiConfig += (myfile.read());
        }
        myfile.close();
    }
    else
    {
        Serial.println("Error opening WiFi config file.");
    }

    Serial.println(wifiConfig.c_str());

    char *p;
    p = strchr(wifiConfig.c_str(), '=');
    esp32config.ssid.append(p + 1);
    Serial.println(esp32config.wifPwd.c_str());

    int b;
    b = (wifiConfig.length() - esp32config.wifPwd.length() - 1);
    
    for (int i = 0; i < b; i++)
    {
        esp32config.ssid += wifiConfig[i];
    }

    Serial.println(esp32config.ssid.c_str());
}