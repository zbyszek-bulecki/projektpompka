#include <FS.h>
#include <SD.h>
#include <SPI.h>

char config[500];

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
    int numberOfLines = 0;
    File myfile = SD.open(configFilePath);
    while (myfile.available())
    {
        if (myfile.read() == '\n')
        {
            numberOfLines++;
        }
    }
    return numberOfLines;
}

char *loadConfiguration(const char *configFilePath)
{
    if (!SD.begin())
    {
        Serial.println("Card Mount Failed");
        return 0;
    }
    File file = SD.open(configFilePath);

    if (file)
    {
        while (file.available())
        {
            for (size_t i = 0; i < 500; i++)
            {
                config[i] = file.read();
                if (config[i] == '\0')
                {
                    file.close();
                }
            }
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
    loadConfiguration(configFilePath);
    int numberOfLines = lineCounter(configFilePath);
    String *configLines[numberOfLines];
    String *configKeys[numberOfLines];
    String *configValues[numberOfLines];
    int j = 0;

    // Serial.println(config);
    // Serial.println(numberOfLines);

    for (size_t i = 0; i < 500; i++)
    {
        while (config[i] != '=')
        {
            configKeys[j]->concat(config[i]);
        }
        while (config[i] != '\n')
        {
            configValues[j]->concat(config[i]);
        }
        if (config[i] == '\n')
        {
            j++;
        }
    }
    for (size_t i = 0; i < numberOfLines; i++)
    {
        Serial.println(configKeys[i]->c_str());
        Serial.println(configValues[i]->c_str());
    }

    // TODO: Parsing single config lines and writing them to struct.
    // TODO: Implement fixed buffer max size 128 bytes or 2x 64 bytes buffer, if more text than 128 bytes, abort reading the line.
    // TODO: Check for correct encoding of text file (UTF-8).
    // TODO: Check if the characters are from the correct ASCII range.
    // TODO: Check if the number of characters in a line does not exceed a reasonable limit.
}