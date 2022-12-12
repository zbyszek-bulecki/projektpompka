#include <Arduino.h>
#include "WiFi.h"
#include "SharkFrameManager.h"

const char* ssid      = "Mort";
const char* password  = "ifiWMam014";

TaskHandle_t taskHandle;

void secondaryLoop();
void secondaryLoopWrapper( void * pvParameters) {
  for(;;) {
    secondaryLoop();
  }
}

void setup(){
  xTaskCreatePinnedToCore(
      secondaryLoopWrapper, /* Function to implement the task */
      "Task1", /* Name of the task */
      10000,  /* Stack size in words */
      NULL,  /* Task input parameter */
      0,  /* Priority of the task */
      &taskHandle,  /* Task handle. */
      1); /* Core where the task should run */

  Serial.begin(115200);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
    Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());
}
 
void loop(){
  Serial.println("Main loop print.");
  delay(5000);
}

void secondaryLoop(){
  const uint16_t port = 1234; // port TCP server
  const char * host = "192.168.0.206"; // ip or dns
  Serial.print("Connecting to ");
  Serial.println(host);
  // Use WiFiClient class to create TCP connections
  WiFiClient client;
  if (!client.connect(host, port)) {
      Serial.println("Connection failed.");
      Serial.println("Waiting 5 seconds before retrying...");
      delay(5000);
      return;
  }

  SharkFrameManager manager = SharkFrameManager(client);

  int loopsBeforeDisconnect = 10;
  for(int i=0; i<loopsBeforeDisconnect; i++){
    String value = "test";
    char b[ 32 ];
    itoa( i, b, 10 );
    value += b;
    manager.send("klucz", value);

    SharkMessage msg = manager.read();

    if(msg.success){
      Serial.print("key: ");
      Serial.println(msg.key.c_str());
      Serial.print("value: ");
      Serial.println(msg.value.c_str());
    }
    else{
      return;
    }

    msg = manager.read();

    if(msg.success){
      Serial.print("key: ");
      Serial.println(msg.key.c_str());
      Serial.print("value: ");
      Serial.println(msg.value.c_str());
    }
    else{
      return;
    }
  }

  Serial.println("Closing connection.");
  client.stop();

  Serial.println("Waiting 10 seconds before restarting...");
  delay(10000);
}