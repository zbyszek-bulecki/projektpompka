#include <FS.h>
#include <SD.h>
#include <SPI.h>

char config[500];
int numberOfLines = 1;

struct esp32config
{
    char ssid[60];
    char wifPwd[60];
    char server_ip_address[60];
    char server_port[60];
    char sleepTime[60];
};

char *loadConfiguration(const char *configFilePath)
{
    if (!SD.begin())
    {
        Serial.println("Card Mount Failed");
        return NULL;
    }
    File file = SD.open(configFilePath);

    for (size_t i = 0; i < 500; i++)
    {
        config[i] = file.read();
        if (config[i] == '\0')
        {
            file.close();
        }
        else
        {
            if (file.read() == '\n')
            {
                numberOfLines++;
            }
        }
    }
    file.close();
    return config;
}

struct esp32config parseConfiguration(const char *configFilePath)
{
    loadConfiguration(configFilePath);
    Serial.println("");
    Serial.println("+++ CONFIG TABLE CONTENTS START +++");
    Serial.println(config);
    Serial.println("+++ CONFIG TABLE CONTENTS END +++");
    Serial.println("");

    esp32config configContainer;
    char configLines[numberOfLines][120]; // TO DO: dynamiczna alokacja pamięci zamiast statycznej
    int row = 0;
    int character = 0;

    memset(configLines[row], 0, 120);
    for (size_t i = 0; i < 500; i++)
    {
        if (config[i] == '\n')
        {
            configLines[row][character + 1] = '\0'; // dodajemy znak końca tablicy
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

    char configKeys[numberOfLines][60];
    char configValues[numberOfLines][60];
    int characterWrite = 0;
    bool flag = 0;

    for (int row2 = 0; row2 < numberOfLines; row2++)
    {
        memset(configKeys[row2], 0, 60);
        memset(configValues[row2], 0, 60);
        for (int characterRead = 0; characterRead < 120; characterRead++)
        {
            if (configLines[row2][characterRead] == '\0')
            {
                configValues[row2][characterWrite] = '\0';
                characterRead = 0;
                characterWrite = 0;
                flag = 0;
                break;
            }
            else
            {
                if (configLines[row2][characterRead] != '=' && flag == 0)
                {
                    configKeys[row2][characterWrite] = configLines[row2][characterRead];
                    characterWrite++;
                }
                else if (configLines[row2][characterRead] == '=')
                {
                    flag = 1;
                    configKeys[row2][characterWrite] = '\0';
                    characterWrite = 0;
                }
                else if (flag == 1)
                {
                    configValues[row2][characterWrite] = configLines[row2][characterRead];
                    characterWrite++;
                }
            }
        }
    }

    char tab1[60];

    for (int row = 0; row <= numberOfLines; row++)
    {
        if (strcmp(configKeys[row], "pwd") == 0)
        {
            memset(configContainer.wifPwd, 0, 60);
            for (size_t character = 0; character < 60; character++)
            {
                configContainer.wifPwd[character] = configValues[row][character];
            }
        }
        else if (strcmp(configKeys[row], "ip") == 0)
        {
            memset(configContainer.server_ip_address, 0, 60);
            for (size_t character = 0; character < 60; character++)
            {
                configContainer.server_ip_address[character] = configValues[row][character];
            }
        }
        else if (strcmp(configKeys[row], "ssid") == 0)
        {
            memset(configContainer.ssid, 0, 60);
            for (size_t character = 0; character < 60; character++)
            {
                configContainer.ssid[character] = configValues[row][character];
            }
        }
        else if (strcmp(configKeys[row], "port") == 0)
        {
            memset(configContainer.server_port, 0, 60);
            for (size_t character = 0; character < 60; character++)
            {
                configContainer.server_port[character] = configValues[row][character];
            }
        }
        else if (strcmp(configKeys[row], "sleepTime") == 0)
        {
            memset(configContainer.sleepTime, 0, 60);
            for (size_t character = 0; character < 60; character++)
            {
                configContainer.sleepTime[character] = configValues[row][character];
            }
        }
    }

    Serial.println(" * * * TESTING configKeys AND configRows ARRAY * * * ");
    for (size_t row3 = 0; row3 < numberOfLines; row3++)
    {
        Serial.println("======");
        Serial.println(configLines[row3]);

        Serial.println("======");
        Serial.println(configKeys[row3]);

        Serial.println("======");
        Serial.println(configValues[row3]);
    }
    Serial.println(" * * * END OF configKeys AND configRows ARRAY TESTING * * * ");
    Serial.println("");

    return configContainer;

    // TODO: Implement fixed buffer max size 128 bytes or 2x 64 bytes buffer, if more text than 128 bytes, abort reading the line.
    // TODO: Check for correct encoding of text file (UTF-8).
    // TODO: Check if the characters are from the correct ASCII range.
    // TODO: Check if the number of characters in a line does not exceed a reasonable limit.
    // TODO: Convert digits from char to int in the class.
}