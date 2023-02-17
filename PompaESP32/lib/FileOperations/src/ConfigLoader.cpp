#include "FS.h"
#include "SD.h"
#include "SPI.h"

const char *WIFI_CONFIG_PATH = "/config/config.txt";

    struct esp32config
  {
    String ssid;
    String wifPwd;
    uint16_t server_ip_address;
    uint16_t server_port;
    uint16_t sleepTime;
  };

esp32config.ssid = "kokoszka";


void loadConfiguration()
{
      if (!SD.begin())
  {
    Serial.println("Card Mount Failed");
    return;
  }
    File myfile = SD.open(WIFI_CONFIG_PATH);
    std::string wifiConfig;

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
    esp32config.
    wifiPwd.append(p + 1);
    Serial.println(wifiPwd.c_str());

    int b;
    b = (wifiConfig.length() - wifiPwd.length() - 1);
    
    for (int i = 0; i < b; i++)
    {
        ssid += wifiConfig[i];
    }

    Serial.println(ssid.c_str());
}