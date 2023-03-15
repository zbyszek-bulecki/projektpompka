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
	if (strcmp(key, "ssid") == 0) {
		ssid = valuePointer;
	}
	else if (strcmp(key, "password") == 0) {
		password = valuePointer;
	}
	else if (strcmp(key, "host") == 0) {
		host = valuePointer;
	}
	else if (strcmp(key, "sleep_time") == 0) {
		sleepTime = atoi(valuePointer);
	}
}
char* Config::getSsid() {
	return ssid;
}
char* Config::getPassword() {
	return password;
}
char* Config::getHost() {
	return host;
}
int Config::getSleepTime() {
	return sleepTime;
}