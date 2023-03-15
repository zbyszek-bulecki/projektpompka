#pragma once
#include <cstring>
#include <stdlib.h>
#include <FS.h>
#include <SD.h>
#include <SPI.h>

class Config {
    size_t memorySize=0;
	char* memory;

	char* ssid;
	char* password;
	char* host;
	int sleepTime;
	
	void loadConfigFile();
    void parseConfigFile();
	void setValueBasedOnKey(char* key, char* valuePointer);
public:
	Config();
	~Config();
	void loadConfig();
	char* getSsid();
	char* getPassword();
	char* getHost();
	int getSleepTime();
};
