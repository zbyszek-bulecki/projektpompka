#include "FS.h"
#include "SD.h"
#include "SPI.h"

std::string config;

struct
{
    std::string ssid;
    std::string wifPwd;
    uint16_t server_ip_address;
    uint16_t server_port;
    uint16_t sleepTime;
} esp32config;

unsigned int lineCounter(const char *configFilePath)
{
    unsigned int number_of_lines = 0;
    FILE *infile = fopen(configFilePath, "r"); //otwiera plik w trybie odczytu
    int ch;

    while (EOF != (ch = getc(infile)))
        if ('\n' == ch)
            ++number_of_lines;
    return number_of_lines;
}

std::string loadConfiguration(const char *configFilePath)
{
    if (!SD.begin())
    {
        Serial.println("Card Mount Failed");
        //powinno być 'return' czy 'return 0'?
        //FIXME write correct return statement
    }
    File myfile = SD.open(configFilePath);

    if (myfile)
    {
        while (myfile.available())
        {
            config += (myfile.read());
        }
        myfile.close();
    }
    else
    {
        Serial.println("Error opening config file.");
    }
    return config;
}

void parseConfiguration()
{

    Serial.println(config.c_str());

    char *p;
    p = strchr(config.c_str(), '='); // tniemy config az napotkamy '='
    esp32config.ssid.append(p + 1);  // dopisujemy do esp32config.ssid wszystko co jest po '='
    // if (strcmp(esp32config.ssid, "ssid") = 0)
    {
    }
    Serial.println(esp32config.wifPwd.c_str());

    int b;
    b = (config.length() - esp32config.wifPwd.length() - 1);

    for (int i = 0; i < b; i++)
    {
        esp32config.ssid += config[i];
    }

    Serial.println(esp32config.ssid.c_str());
}