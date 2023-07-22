#include "Config.h"

#define KEY_BUFFOR 60
#define PATH "/config/config.txt"

Config::Config() {}
Config::~Config() {
	delete memory;
}
void Config::loadConfigFile() {	
    if(!SD.begin()){
        Serial.println("Card Mount Failed");
        return;
    }

    File file = SD.open(PATH);
    if(!file) return;

    memorySize = file.available();
    memory = new char[memorySize+1];
    memset(memory, '\0', memorySize+1);

    for(int i=0; i<memorySize; i++){
        memory[i] = file.read();       
    }

    file.close();
}
void Config::loadConfig() {
    loadConfigFile();
    parseConfigFile();
}
void Config::parseConfigFile() {
	bool readingKey = true;
	char* keyPointer = memory;
	char* valuePointer = NULL;

	for (int i = 0; i < memorySize; i++) {		
		if (readingKey) {			
			if (memory[i] == '=') {
				memory[i] = '\0';
				readingKey = !readingKey;
				valuePointer = memory + i + 1;
			}
			else if (memory[i] == '\0') {
				break;
			}				
		}
		else {
			if (memory[i] == '\n' || memory[i] == '\0') {
				readingKey = !readingKey;
				memory[i] = '\0';
				setValueBasedOnKey(keyPointer, valuePointer);
				keyPointer = memory + i + 1;
			}
            if(memory[i] == '\r' && memory[i+1] == '\n'){
                readingKey = !readingKey;
				memory[i++] = '\0';
                memory[i] = '\0';
				setValueBasedOnKey(keyPointer, valuePointer);
				keyPointer = memory + i + 1;				
            }
		}
	}
}

void Config::setValueBasedOnKey(char* key, char* valuePointer) {
	configs.insert(std::pair<char*, char*>(key, valuePointer));
}
char* Config::get(char* key) {
	return configs[key];
}
char* Config::get(const char* key) {
	return get(strdup(key));
}
int Config::getInt(char* key) {
	return atoi(get(key));
}
int Config::getInt(const char* key) {
	return getInt(strdup(key));
}
float Config::getFloat(char* key) {
	return atof(get(key));
}
float Config::getFloat(const char* key) {
	return getFloat(strdup(key));
}