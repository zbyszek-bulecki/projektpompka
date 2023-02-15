#include "FS.h"
#include "SD.h"
#include "SPI.h"

const char *WIFI_CONFIG_PATH = "/config/wifi.txt";

void loadConfiguration()
{
    
      if (!SD.begin())
  {
    Serial.println("Card Mount Failed");
    return;
  }
    File myfile = SD.open(WIFI_CONFIG_PATH);
    std::string wifiConfig;
    std::string wifiPassword;
    std::string ssid;

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
    wifiPassword.append(p + 1);
    Serial.println(wifiPassword.c_str());

    int b;
    b = (wifiConfig.length() - wifiPassword.length() - 1);
    
    for (int i = 0; i < b; i++)
    {
        ssid += wifiConfig[i];
    }

    Serial.println(ssid.c_str());
}

// struct esp32config
//   {
//     String ssid;
//     String wifPwd;
//     String server_name;
//     uint16_t server_ip_address;
//     uint16_t server_port;
//     uint16_t sleepTime;
//   };