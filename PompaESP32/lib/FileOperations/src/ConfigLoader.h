#ifndef ConfigLoader_H
#define ConfigLoader_H

#include "FS.h"
#include "SD.h"
#include "SPI.h"

std::string loadConfiguration(const char *configFilePath);

unsigned int lineCounter(const char *configFilePath);

#endif