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
        return NULL;
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
        return NULL;
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
        Serial.println(" - - - - - - - - ");
        Serial.println(configKeys[row2]);
        Serial.println(configValues[row2]);
        Serial.println(" - - - - - - - - ");
        for (int characterRead = 0; characterRead < 120; characterRead++)
        {
            if (configLines[row2][characterRead] == '\n')
            {
                configValues[row2][characterWrite] = '\0';
                characterRead = 0;
                characterWrite = 0;
                flag = 0;
                break;
            }
            else
            {
                if (flag == 0 && configLines[row2][characterRead] != '=')
                {
                    configKeys[row2][characterWrite] = configLines[row2][characterRead];
                    Serial.println(configKeys[row2][characterWrite]);
                    characterWrite++;
                }
                else if (configLines[row2][characterRead] == '=') // w takim wypadku sprawdza IFy tylko do spełnienia pierwszego warunku
                {
                    flag = 1;
                    characterWrite = 0;
                    configKeys[row2][characterWrite] = '\0';
                }
                else if (flag == 1)
                {
                    configValues[row2][characterWrite] = configLines[row2][characterRead];
                    characterWrite++;
                }
            }
        }
    }

    Serial.println("======");
    Serial.println(configLines[0]);

    Serial.println("======");
    Serial.println(configKeys[0]);

    Serial.println("======");
    Serial.println(configValues[0]);

    Serial.println("======");
    Serial.println(configLines[1]);

    Serial.println("======");
    Serial.println(configKeys[1]);

    Serial.println("======");
    Serial.println(configValues[1]);

    // TODO: Parsing single config lines and writing them to struct.
    // TODO: Implement fixed buffer max size 128 bytes or 2x 64 bytes buffer, if more text than 128 bytes, abort reading the line.
    // TODO: Check for correct encoding of text file (UTF-8).
    // TODO: Check if the characters are from the correct ASCII range.
    // TODO: Check if the number of characters in a line does not exceed a reasonable limit.
}