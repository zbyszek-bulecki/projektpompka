#include <string>
#include <Arduino.h>
#include "WiFi.h"

const short PING_OPCODE = 0;
const short PONG_OPCODE = 1;
const short MSG_OPCODE = 2;

struct Buffer{
    int size;
    uint8_t* data;
};

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
        long remainsSize;
        uint8_t *remains;
    public: 
        SharkFrameManager(WiFiClient& client);
        void stashRemains(Buffer buffer, int from);
        SharkMessage read();
        SharkMessageHeader readHeader(Buffer buffer);
        Buffer mergeRemainsWithBuffer(uint8_t *buffer, size_t lenght);
        void send(String key, String value);
        void sendPong();
        void createHeader(char *frame, boolean fin, short opcode, int payloadSize);
};