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
        if(myfile.read() >= 0x20 && myfile.read() <= 0x7e)
        {
        if (myfile.read() == '\n')
        {
            numberOfLines++;
        }
        }
    }
    Serial.println(numberOfLines);
    return numberOfLines + 1;
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

// char parseLines(char *configKeys[], char *config[]){

// }

// char parseRows( char *configRows[], char *config[]){

// }

void parseConfiguration(const char *configFilePath)
{
    Serial.println(loadConfiguration(configFilePath));

    int numberOfLines = 8;
    char configLines[numberOfLines][120];
    char configKeys[numberOfLines][60];
    char configValues[numberOfLines][60];
    Serial.println(numberOfLines);
    int row = 0;
    int character = 0;
    int lineCounter = 0;

    for (size_t i = 0; i < 500; i++)
    {
        if(config[i] >= 0x20 && config[i] <= 0x7e)
        {
        configLines[row][character] = config[i];
        }

        character++;
        if (config[i] == '\n')
        {
            row++;
        }
        
        if (character == 119)
        {
            break;
        }
        
    }
    Serial.println("======");
    Serial.println(configLines[0]);
    Serial.println(configLines[1]);
    Serial.println(configLines[2]);
    Serial.println(configLines[3]);
    Serial.println(configLines[4]);
    //first divide config into lines, then parse them

    // for (size_t i = 0; i < 500; i++)
    // {
    //     Serial.println("o ");

    //     if (config[i] != '=' && config[i] != '\0')
    //     {
    //         configKeys[j][i] = config[i];
    //         Serial.print("1st loop ");
    //     }
    //     if (config[i] != '\n' && config[i] != '\0')
    //     {
    //         configValues[j][i] = config[i];
    //         Serial.print("2nd loop ");
    //     }
    //     if (config[i] == '\n')
    //     {
    //         j++;
    //         Serial.print("the J-if ");
    //     }
    // }
    // for (size_t l = 0; l < numberOfLines; l++)
    // {
    //     Serial.println(configKeys[l]);
    //     Serial.println(configValues[l]);
    // }

    // TODO: Parsing single config lines and writing them to struct.
    // TODO: Implement fixed buffer max size 128 bytes or 2x 64 bytes buffer, if more text than 128 bytes, abort reading the line.
    // TODO: Check for correct encoding of text file (UTF-8).
    // TODO: Check if the characters are from the correct ASCII range.
    // TODO: Check if the number of characters in a line does not exceed a reasonable limit.
}