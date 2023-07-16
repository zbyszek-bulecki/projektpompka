#pragma once
#include <cstring>
#include <stdlib.h>
#include <FS.h>
#include <SD.h>
#include <SPI.h>
#include <map>

class Config {
    size_t memorySize=0;
	char* memory;
	std::map<char*, char*> configs;

	void loadConfigFile();
    void parseConfigFile();
	void setValueBasedOnKey(char* key, char* valuePointer);
public:
	Config();
	~Config();
	void loadConfig();
	char* get(const char* key);
	char* get(char* key);
	int getInt(char* key);
	int getInt(const char* key);
	float getFloat(char* key);
	float getFloat(const char* key);
};
