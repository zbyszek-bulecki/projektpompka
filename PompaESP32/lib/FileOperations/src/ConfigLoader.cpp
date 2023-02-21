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
    if (!SD.begin())
        Serial.println("Card Mount Failed");

    unsigned int number_of_lines = 0;
    FILE *infile = fopen(configFilePath, "r"); // opens file in read-only mode
    int ch;

    while (EOF != (ch = getc(infile)))
        if ('\n' == ch)
            ++number_of_lines;
    return number_of_lines;
}

std::string loadConfiguration(const char *configFilePath)
{
    File file = SD.open(configFilePath);

    if (file)
    {
        while (file.available())
        {
            config += (file.read());
        }
        file.close();
    }
    else
    {
        Serial.println("Error opening config file.");
    }
    return config;
}

void parseConfiguration(const char *configFilePath)
{
    unsigned int numberOfLines = lineCounter(configFilePath);
    loadConfiguration(configFilePath);
    std::string configLines[numberOfLines];
    char endOfLine[2] = {'/', 'n'};

    for (size_t i = 0; i < numberOfLines; i++)
    {
        configLines[i] = config.substr(0,config.find(endOfLine));
        config = config.erase(configLines[i].size());
        Serial.println(configLines[i].c_str());        
    }
}