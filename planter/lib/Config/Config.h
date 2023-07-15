#pragma once
#include <cstring>
#include <stdlib.h>
#include <FS.h>
#include <SD.h>
#include <SPI.h>

class Config {
    size_t memorySize=0;
	char* memory;

	char* wifiSsid;
	char* wifiPassword;
	char* host;
	char* username;
	char* password;
	int sleepTime;
	
	void loadConfigFile();
    void parseConfigFile();
	void setValueBasedOnKey(char* key, char* valuePointer);
public:
	Config();
	~Config();
	void loadConfig();
	char* getWifiSsid();
	char* getWifiPassword();
	char* getHost();
	char* getUsername();
	char* getPassword();
	int getSleepTime();
};
