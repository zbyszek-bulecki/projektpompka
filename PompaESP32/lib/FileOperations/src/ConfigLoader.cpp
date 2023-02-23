#include <FS.h>
#include <SD.h>
#include <SPI.h>

std::string config;

struct
{
    std::string ssid;
    std::string wifPwd;
    uint16_t server_ip_address;
    uint16_t server_port;
    uint16_t sleepTime;

} esp32config;

int lineCounter(const char *configFilePath)
{
    // if (!SD.begin())
    // {
    //     Serial.println("Card mount failed.");
    //     return -1;
    // }

    int numberOfLines = 0;
    File myfile = SD.open("/config/config.txt");
    while(myfile.available()){
        if(myfile.read() == '\n'){
            numberOfLines++;
        }
        }
    Serial.println(numberOfLines);
    return numberOfLines;
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

        // FIXME: Exception handling when reading from SD card.
    }

    void parseConfiguration(const char *configFilePath)
    {
        unsigned int numberOfLines = lineCounter(configFilePath);
        loadConfiguration(configFilePath);
        std::string configLines[numberOfLines];
        char endOfLine[2] = {'/', 'n'};

        for (size_t i = 0; i < numberOfLines; i++)
        {
            configLines[i] = config.substr(0, config.find(endOfLine));
            config = config.erase(configLines[i].size());
            Serial.println(configLines[i].c_str());
            // TODO: Parsing single config lines and writing them to struct.
            // TODO: Implement fixed buffer max size 128 bytes or 2x 64 bytes buffer, if more text than 128 bytes, abort reading the line

        }
    }