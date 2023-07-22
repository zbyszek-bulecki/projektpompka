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
    size_t memorySize=0;
	char* memory;
	std::map<char*, char*, keyComparator> configs;

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
