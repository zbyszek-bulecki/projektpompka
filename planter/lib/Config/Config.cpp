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
	char keyBuffer[KEY_BUFFOR];
	char* valuePointer = NULL;
	memset(keyBuffer, 0, KEY_BUFFOR);
	int keyBufferPointer = 0;

	for (int i = 0; i < memorySize; i++) {		
		if (readingKey) {			
			if (memory[i] == '=') {
				readingKey = !readingKey;
				keyBufferPointer = 0;
				valuePointer = memory + i + 1;
			}
			else if (memory[i] == '\0') {
				break;
			}				
			else {
				keyBuffer[keyBufferPointer++] = memory[i];
			}
		}
		else {
			if (memory[i] == '\n' || memory[i] == '\0') {
				readingKey = !readingKey;
				memory[i] = '\0';
				setValueBasedOnKey(keyBuffer, valuePointer);
				memset(keyBuffer, 0, KEY_BUFFOR);
			}
            if(memory[i] == '\r' && memory[i+1] == '\n'){
                readingKey = !readingKey;
				memory[i++] = '\0';
                memory[i] = '\0';
				setValueBasedOnKey(keyBuffer, valuePointer);
				memset(keyBuffer, 0, KEY_BUFFOR);
            }
		}
	}
}

void Config::setValueBasedOnKey(char* key, char* valuePointer) {
	configs[key] = valuePointer;
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