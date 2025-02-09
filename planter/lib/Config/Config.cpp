#include "Config.h"

#define KEY_BUFFOR 60
#define PATH "/config/config.txt"

char* copyChar(const char* input) {
    if (!input) return nullptr;
    size_t len = std::strlen(input);
    char* output = new char[len + 1];
    std::strcpy(output, input);
    return output;
}
Config::Config() {}
Config::~Config() {
	std::map<char*, char*, keyComparator>::iterator it;
    for (it = configs.begin(); it != configs.end(); ++it) {
        delete[] it->first;
        delete[] it->second;
    }
	configs.clear();
}
void Config::loadConfigFile() {
     if(!SD.begin()){
        Serial.println("Card Mount Failed");
        return;
    }

    File file = SD.open(PATH);
    if(!file) return;

    size_t memorySize = file.available();
    char* tmpMemory = new char[memorySize+1];
    memset(tmpMemory, '\0', memorySize+1);

    for(int i=0; i<memorySize; i++){
        tmpMemory[i] = file.read();       
    }
	
	parseConfigFile(memorySize, tmpMemory);

    file.close();
	delete tmpMemory;
}
void Config::parseConfigFile(size_t memorySize, char* tmpMemory) {
	bool readingKey = true;
	char* keyPointer = tmpMemory;
	char* valuePointer = NULL;

	for (int i = 0; i < memorySize; i++) {
        if (readingKey) {
            if (tmpMemory[i] == '=') {
                tmpMemory[i] = '\0';
                readingKey = false;
                valuePointer = tmpMemory + i + 1;

                // Dynamically allocate memory for the key
                char* newKey = new char[std::strlen(keyPointer) + 1];
                std::strcpy(newKey, keyPointer);
                keyPointer = newKey;
            } else if (tmpMemory[i] == '\0') {
                break;
            }
        } else {
            if (tmpMemory[i] == '\n' || tmpMemory[i] == '\0') {
                tmpMemory[i] = '\0';

                // Store the key-value pair
                storeKeyValuePair(keyPointer, valuePointer);

                readingKey = true;
                keyPointer = tmpMemory + i + 1;
            }
            else if (tmpMemory[i] == '\r' && tmpMemory[i + 1] == '\n') {
                tmpMemory[i++] = '\0';
                tmpMemory[i] = '\0';

                // Store the key-value pair
                storeKeyValuePair(keyPointer, valuePointer);

                readingKey = true;
                keyPointer = tmpMemory + i + 1;
            }
        }
    }
}
void Config::storeKeyValuePair(char* keyPointer, char* valuePointer) {
    // Dynamically allocate memory for the key
    char* newKey = new char[std::strlen(keyPointer) + 1];
    std::strcpy(newKey, keyPointer);

    // Dynamically allocate memory for the value
    char* newValue = new char[std::strlen(valuePointer) + 1];
    std::strcpy(newValue, valuePointer);

    // Store the key-value pair
    setValueBasedOnKey(newKey, newValue);
}
void Config::setValueBasedOnKey(char* key, char* valuePointer) {
	configs.insert(std::pair<char*, char*>(key, valuePointer));
}
bool Config::has(char* key) {
	return configs.find(key) == configs.end() ? false : true;
}
bool Config::has(const char* key) {
    char* k = strdup(key);
	bool flag = has(k);
    free(k);
    return flag;
}
char* Config::get(char* key) {
	return configs[key];
}
char* Config::get(const char* key) {
    char* k = strdup(key);
	char* pointer = get(k);
    free(k);
    return pointer;
}
void Config::set(const char* key, const char* value) {
    if (!key || !value) return; // Validate input

    // Look for an existing key
    auto it = configs.find(const_cast<char*>(key));
    if (it != configs.end()) {
        // Key exists, update the value
        delete[] it->second; // Free the old value
        it->second = copyChar(value); // Copy the new value
    } else {
        // Insert a new key-value pair
        configs.insert(std::make_pair(copyChar(key), copyChar(value)));
    }
}
int Config::getInt(char* key) {
	return atoi(get(key));
}
int Config::getInt(const char* key) {
    char* k = strdup(key);
	int value = getInt(k);
    free(k);
    return value;
}
void Config::set(const char* key, int value) {
    char buffer[20]; // Allocate enough space for an integer string
    std::snprintf(buffer, sizeof(buffer), "%d", value); // Convert int to string
    set(key, buffer); // Use the string-based set method
}
float Config::getFloat(char* key) {
	return atof(get(key));
}
float Config::getFloat(const char* key) {
    char* k = strdup(key);
	float value = getFloat(k);
    free(k);
    return value;
}
void Config::set(const char* key, float value) {
    char buffer[20]; // Allocate enough space for a float string
    std::snprintf(buffer, sizeof(buffer), "%.2f", value); // Convert float to string with two decimals
    set(key, buffer); // Use the string-based set method
}
void Config::saveConfigFile() {
    File file = SD.open(PATH, FILE_WRITE);
    if (!file) {
        Serial.println("Failed to open config file for writing");
        return;
    }

    for (const auto& pair : configs) {
        file.print(pair.first);
        file.print("=");
        file.println(pair.second);
    }

    file.close();
}