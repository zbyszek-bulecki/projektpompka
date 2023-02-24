#ifndef ConfigLoader_H
#define ConfigLoader_H

#include "FS.h"
#include "SD.h"
#include "SPI.h"

char * loadConfiguration(const char *configFilePath);

int lineCounter(const char *configFilePath);

void parseConfiguration(const char *configFilePath);

#endif