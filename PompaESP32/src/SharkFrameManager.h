#include <string>
#include <Arduino.h>
#include "WiFi.h"

const short PING_OPCODE = 0;
const short PONG_OPCODE = 1;
const short MSG_OPCODE = 2;

struct SharkMessageHeader{
    bool fin;
    short opcode;
    int payloadSize;
};

struct SharkMessage{
    boolean success;
    String key;
    String value;
};

class SharkFrameManager{
        WiFiClient client;
    public: 
        SharkFrameManager(WiFiClient& client);
        SharkMessage read();
        SharkMessage readHeader();
        void send(String key, String value);
};