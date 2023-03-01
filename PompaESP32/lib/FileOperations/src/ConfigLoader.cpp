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
    int numberOfLines = 1;
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
                if (config[i] == EOF)
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
    Serial.println(loadConfiguration(configFilePath));

    int numberOfLines = lineCounter(configFilePath);
    char configLines[numberOfLines][120]; // TO DO: dynamiczna alokacja pamięci zamiast statycznej
    int row = 0;
    int character = 0;

    memset(configLines[row], 0, 120);

    for (size_t i = 0; i < 500; i++)
    {
        if (config[i] == '\n')
        {
            configLines[row][character + 1] = '\0';
            row++;
            character = 0;
            memset(configLines[row], 0, 120);
        }
        else
        {
            configLines[row][character] = config[i];
            character++;
        }

        if (character >= 119)
        {
            break;
        }
    }

    row = 0;
    character = 0;
    row = 0;
    char configKeys[numberOfLines][60];
    char configValues[numberOfLines][60];
    bool flag = 0;
    memset(configKeys[row], 0, 60);
    memset(configValues[row], 0, 60);

    for (size_t character = 0; character < 120; character++)
    {
        if (configLines[row][character] == '\n')
        {
            configLines[row][character + 1] = '\0';
            row++;
            character = 0;
            memset(configKeys[row], 0, 60);
            memset(configValues[row], 0, 60);
        }
        else
        {
            if (configLines[row][character] != '=' && flag == 0)
            {
                configKeys[row][character] = configLines[row][character];
            }
            if (configLines[row][character] == '=')
            {
                flag = 1;
                // character++;
            }
            if (flag == 1)
            {
            configValues[row][character] = configLines[row][character];
            }
        }
    }

    Serial.println("======");
    Serial.println(configLines[0]);

    Serial.println("======");
    Serial.println(configKeys[0]);

    Serial.println("======");
    Serial.println(configValues[0]);

    Serial.println("====== FLAG ======");
    Serial.println(flag);

    // TODO: Parsing single config lines and writing them to struct.
    // TODO: Implement fixed buffer max size 128 bytes or 2x 64 bytes buffer, if more text than 128 bytes, abort reading the line.
    // TODO: Check for correct encoding of text file (UTF-8).
    // TODO: Check if the characters are from the correct ASCII range.
    // TODO: Check if the number of characters in a line does not exceed a reasonable limit.
}