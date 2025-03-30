#include <SPI.h>
#include <SD.h>

void setup()
{
    // Initialize SD card
    if (!SD.begin())
    {
        Serial.println("Card Mount Failed");
        return;
    }

    // Define the string to save to the SD card
    char *data = "Hello, this is a test string saved to the SD card!";

    // Save the string to the SD card
    saveToSD("testfile.txt", data);
}

void saveToSD(const char *filename, const char *data)
{
    // Open the file for writing
    File file = SD.open(filename, FILE_WRITE);

    // Check if the file was opened successfully
    if (!file)
    {
        Serial.println("Failed to open file for writing.");
        return;
    }

    // Write the data to the file
    file.println(data);
    Serial.println("Data written to file successfully.");

    // Close the file
    file.close();
}