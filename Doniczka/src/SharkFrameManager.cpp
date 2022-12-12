#include "SharkFrameManager.h"

const short HEADER_SIZE = 3;

SharkFrameManager::SharkFrameManager(WiFiClient& client){
  this->client = client;
  this->remains = {};
  this->remainsSize = 0;
}

void SharkFrameManager::stashRemains(Buffer buffer, int from){
  this->remains = new uint8_t[buffer.size - from];
  std::copy(buffer.data + from, buffer.data + buffer.size, this->remains);
  this->remainsSize = buffer.size - from;
}

SharkMessage SharkFrameManager::read(){
  SharkMessage message;
  message.success=false;
  message.key="";
  message.value="";

  Serial.println("waiting...");
  int maxloops = 0;  
  int expectedLenght = 3;
  Serial.println("=========================================================================");
  while(!message.success && maxloops < 10000 && client.connected()){  
    delay(1);
    int available = client.available();
    if (available + this->remainsSize > expectedLenght){

      Serial.print(">>");
      Serial.print(available);
      Serial.print("::");
      Serial.println(this->remainsSize);

      Serial.println("parsing message...");
      uint8_t *rawBuffer;
      size_t lenght;
      if(available>0){
        Serial.println("reading from buffer");
        rawBuffer = new uint8_t[8000];
        lenght = client.readBytes(rawBuffer, (size_t)8000);
      }
      else{
        Serial.println("reading from remains");
        rawBuffer = {};
        lenght = 0;  
      }
      Buffer buffer = this->mergeRemainsWithBuffer(rawBuffer, lenght);
      SharkMessageHeader header = this->readHeader(buffer);

      Serial.print(">>>>>>");
      Serial.print(HEADER_SIZE);
      Serial.print("+");
      Serial.print(header.payloadSize);
      Serial.print("?");
      Serial.println(buffer.size);


      if(buffer.size < HEADER_SIZE + header.payloadSize){
        expectedLenght = buffer.size - HEADER_SIZE - header.payloadSize;
        //this->stashRemains(buffer, HEADER_SIZE + header.payloadSize);
        Serial.print("not enough");
        continue;
      }

      Serial.print(">>>>");
      Serial.println(buffer.size);

      delete this->remains;
      this->stashRemains(buffer, HEADER_SIZE + header.payloadSize);
      std::copy(buffer.data + HEADER_SIZE + header.payloadSize, buffer.data + buffer.size, this->remains);

      Serial.print(">>>>");
      Serial.println(this->remainsSize);

      if(header.opcode==PING_OPCODE){
        this->sendPong();
        continue;
      }

      String payload(buffer.data + HEADER_SIZE, header.payloadSize);
      int separatorPosition = payload.indexOf(':');

      message.success = true;
      message.key = payload.substring(0, separatorPosition);
      message.value = payload.substring(separatorPosition + 1, header.payloadSize);

      delete rawBuffer;
    }
  }

  if(maxloops >= 1000){
    Serial.println("client.available() timed out ");
  }

  return message;
}

Buffer SharkFrameManager::mergeRemainsWithBuffer(uint8_t *buffer, size_t lenght){
  int combinedLenght = this->remainsSize + lenght;
  uint8_t * data = new uint8_t[combinedLenght];
  std::copy(this->remains, this->remains + remainsSize, data);
  std::copy(buffer, buffer + lenght, data + this->remainsSize);  

  Buffer merged;
  merged.size = this->remainsSize + lenght;
  merged.data = data;
  
  this->remains = {};
  this->remainsSize = 0;
  return merged;
}

SharkMessageHeader SharkFrameManager::readHeader(Buffer buffer){
  boolean fin = (buffer.data[0] & 128) >> 7 == 1 ? true : false;  
  unsigned int opcode = (unsigned int)(buffer.data[0] & 15);
  unsigned int payloadSize = 0;
  for(int i=0; i<HEADER_SIZE-1; i++){
      payloadSize += ((unsigned int)buffer.data[i+1] << (i * 8));
  }

  Serial.print("fin: ");
  Serial.println(fin);
  Serial.print("opcode: ");
  Serial.println(opcode);
  Serial.print("payloadSize: ");
  Serial.println(payloadSize); 

  SharkMessageHeader header;
  header.fin = fin;
  header.opcode = opcode;
  header.payloadSize = payloadSize;
  return header;
}

void SharkFrameManager::send(String key, String value){
    if(!client.connected()){
      return;
    }

    long payloadSize = key.length() + 1 + value.length();
    if(payloadSize > INT32_MAX){
        Serial.println("Message too big");
        return;
    }

    bool fin = true;
    short opcode = MSG_OPCODE;

    char* buffer = new char[HEADER_SIZE + payloadSize];
    this->createHeader(buffer, fin, opcode, payloadSize);

    const char* keyBytes = key.c_str();
    const char* valueBytes = value.c_str();  

    std::copy(keyBytes, keyBytes + key.length(), buffer + HEADER_SIZE);
    buffer[HEADER_SIZE + key.length()] = 58;
    std::copy(valueBytes, valueBytes + value.length(), buffer + HEADER_SIZE + 1 + key.length());
    
    String frame(buffer, HEADER_SIZE+payloadSize);
    this->client.print(frame);
    delete buffer;
}

void SharkFrameManager::sendPong(){
  Serial.println("sending pong");
  char* buffer = new char[HEADER_SIZE];
  this->createHeader(buffer, true, PONG_OPCODE, 0);
  String frame(buffer, HEADER_SIZE);
  this->client.print(frame);
  delete buffer;
}

void SharkFrameManager::createHeader(char *frame, boolean fin, short opcode, int payloadSize){
  frame[0] = 0;
  if(fin){
      frame[0] += 128;
  }
  frame[0] += opcode;
  for(int i=0; i<HEADER_SIZE-1; i++){
      frame[HEADER_SIZE-1-i] = (payloadSize >> (i * 8));
  }
}