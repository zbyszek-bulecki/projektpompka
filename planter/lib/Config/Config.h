#pragma once
#include <cstring>
#include <stdlib.h>
#include <FS.h>
#include <SD.h>
#include <SPI.h>
#include <map>

struct keyComparator {
    bool operator()(char* a, char* b) const {
        for ( ; *a != '\0' && *b != '\0'; ++a, ++b ) {
			if (tolower(*a) != tolower(*b)) {
				return (tolower(*a) < tolower(*b));
			}
			else if (*a != *b){
				if (*(a+1) == '\0' && *(b+1) == '\0') {
					return (*a < *b);
				}
			}
		}
		return (tolower(*a) < tolower(*b));
    }
};

class Config {
	std::map<char*, char*, keyComparator> configs;
    void parseConfigFile(size_t memorySize, char* tmpMemory);
	void storeKeyValuePair(char* keyPointer, char* valuePointer);
	void setValueBasedOnKey(char* key, char* valuePointer);
public:
	Config();
	~Config();
	void loadConfigFile();
	bool has(const char* key);
	bool has(char* key);
	char* get(const char* key);
	char* get(char* key);
	void set(const char* key, const char* value);
	int getInt(char* key);
	int getInt(const char* key);
	void set(const char* key, int value);
	float getFloat(char* key);
	float getFloat(const char* key);
	void set(const char* key, float value);
	void saveConfigFile();
};
