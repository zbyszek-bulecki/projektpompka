#include <Arduino.h>
#include "Config.h"
#include "RestClient.h"

int i = 0;

RestClient* client;

void get(){
  Serial.println("***********GET***********");
  Response response = client->sendGet("/sensors");
  const char* field = (*response.payload)["field"];
  Serial.print("field: ");
  Serial.println(field);
  
  Serial.println("array: ");
  const JsonArray arr = (*response.payload)["array"];
  for(JsonVariant v : arr) {
    Serial.print(" - ");
    Serial.println(v.as<String>());
  }

  const boolean flag = (*response.payload)["flag"];
  Serial.print("flag: ");
  Serial.println(flag);
  
  client->flushResponse(response);
}

void post(){
    Serial.println("***********POST***********");
  DynamicJsonDocument* request = new DynamicJsonDocument(REST_PAYLOAD_SIZE);

  i = (i + 1)%10;
  (*request)["array"][0] = String("va")+i;
  (*request)["array"][1] = "va2";
  (*request)["flag"] = true;

  Serial.println("Sending...");
  Response response = client->sendPost("/sensors", request);
  delete request;

  const char* field = (*response.payload)["field"];
  Serial.print("field: ");
  Serial.println(field);
  
  Serial.println("array: ");
  const JsonArray arr = (*response.payload)["array"];
  for(JsonVariant v : arr) {
    Serial.print(" - ");
    Serial.println(v.as<String>());
  }

  const boolean flag = (*response.payload)["flag"];
  Serial.print("flag: ");
  Serial.println(flag);

  client->flushResponse(response);
}

void executeProcedure(){
  Serial.println(ESP.getFreeHeap());

  Config* config = new Config();
  config->loadConfig();
  Serial.print("wifi_ssid:");
  Serial.println(config->getWifiSsid());
  Serial.print("wifi_password:");
  Serial.println(config->getWifiPassword());
  Serial.print("username:");
  Serial.println(config->getUsername());
  Serial.print("password:");
  Serial.println(config->getPassword());
  Serial.print("host:");
  Serial.println(config->getHost());
  Serial.print("sleep_time:");
  Serial.println(config->getSleepTime());
  
  char* wifiSsid = config->getWifiSsid();
  char* wifiPassword = config->getWifiPassword();
  char* host = config->getHost();
  char* username = config->getUsername();
  char* password = config->getPassword();
  client = new RestClient(wifiSsid, wifiPassword, host);
  client->withBasicAuthentication(username, password);
  client->setup();
  get();
  post();

  delete client;
  delete config;
  
  sleep(5);
}

void setup(){
  Serial.begin(115200); 
  sleep(2);
  executeProcedure();
  Serial.println("");
}

void loop(){
  Serial.print(".");
  sleep(5);
}
