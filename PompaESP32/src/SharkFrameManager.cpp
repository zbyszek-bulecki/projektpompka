#include "SharkFrameManager.h"

const short HEADER_SIZE = 3;

SharkFrameManager::SharkFrameManager(WiFiClient& client){
    this->client = client;
}

SharkMessage SharkFrameManager::read(){
  SharkMessage message;
  message.success=false;
  message.key="";
  message.value="";

  int maxloops = 0;  
  while (!client.available() && maxloops < 1000){
    maxloops++;
    delay(1); //delay 1 msec
  }

  if (client.available() > 0){
    uint8_t *buffer = new uint8_t[8000];
    size_t lenght = client.readBytes(buffer, (size_t)8000);
    /*
      f000oooo
      pppppppp
      pppppppp
    */

    // 10001011 & 10000000 => 10000000 >> 7 => 00000001
    int fin = (buffer[0] & 128) >> 7;  
    // 10001011 & 00001111 => 00001011
    unsigned int opcode = (unsigned int)(buffer[0] & 15);
    // tablica bajtów = 00000001 00000111
    // 00000001 => 00000001 //255 skok o 1
    // 00000111 => 00000111 00000000 // 256 - 65281 skok o 255

    //  1234
    //  12 i 34
    //  34
    //  1200
    //  1234

    // 65536 - 255 = 65281

    // int = 00000111 00000001
    unsigned int payloadSize = 0;
    for(int i=0; i<HEADER_SIZE-1; i++){
        payloadSize += ((unsigned int)buffer[i+1] << (i * 8));
    }

    Serial.print("fin: ");
    Serial.println(fin);
    Serial.print("opcode: ");
    Serial.println(opcode);
    Serial.print("payloadSize: ");
    Serial.println(payloadSize); 

    String payload(buffer+HEADER_SIZE, lenght);
    int separatorPosition = payload.indexOf(':');

    message.success = true;
    message.key = payload.substring(0, separatorPosition);
    message.value = payload.substring(separatorPosition + 1, payloadSize);

    delete buffer;
  }
  else{
    Serial.println("client.available() timed out ");
  }
  return message;
}

SharkMessage SharkFrameManager::readHeader(){
  
}

void SharkFrameManager::send(String key, String value){
    long payloadSize = key.length() + 1 + value.length();
    if(payloadSize > INT32_MAX){
        Serial.println("Message too big");
        return;
    }

    bool fin = true; //todo
    short messageType = MSG_OPCODE;

    char* buffor = new char[HEADER_SIZE + payloadSize]; // ramka = tablica o długości header size + payload size

    //bajt z fin i opcode
    buffor[0] = 0;
    if(fin){
        buffor[0] += 128; // 10000000
    }
    buffor[0] += messageType; // 00000001 => 10000001

    //wyznaczenie bajtów odpowiedzialny za dlugość
    for(int i=0; i<HEADER_SIZE-1; i++){
        buffor[HEADER_SIZE-1-i] = (payloadSize >> (i * 8));
    }

    // String - obiekt (tablicy charów jest niemutowalna)

    // rzutuje string na tablice bytów
    const char* keyBytes = key.c_str();
    const char* valueBytes = value.c_str();  

    // kopiowanie klucza do ramki
    // odkąd, dokąd, gdzie zacząć zapisywać
    std::copy(keyBytes, keyBytes + key.length(), buffor + HEADER_SIZE);

    // wstawienie separatora
    buffor[HEADER_SIZE + key.length()] = 58; // 58 = ":"

    // kopiowanie wartości do ramki
    std::copy(valueBytes, valueBytes + value.length(), buffor + HEADER_SIZE + 1 + key.length());
    
    // wysyłanie
    String frame(buffor, HEADER_SIZE+payloadSize);
    this->client.print(frame);
    delete buffor;
}