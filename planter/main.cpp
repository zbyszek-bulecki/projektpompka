#include <Arduino.h>
#include "Config.h"
#include "RestClient.h"



RestClient* client;

/*
GET EXAMPLE

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

POST EXAMPLE

int i = 0;
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
*/

void postMeasurements(){
  Serial.println("***********POST***********");
  DynamicJsonDocument* request = new DynamicJsonDocument(REST_PAYLOAD_SIZE);

  (*request)["name"] = "dev1";
  (*request)["macAddress"] = "213344";
  (*request)["soilMoisture"] = 0;
  (*request)["lightIntensity"] = 0;
  (*request)["temperature"] = 0;
  (*request)["pressure"] = 0;
  (*request)["waterLevel"] = 0;

  Serial.println("Sending...");
  Response response = client->sendPost("/planter/measurements", request);
  delete request;

  if(response.statusCode==200){
    Serial.println("Data send successfully!");
  }
  else{
    Serial.print("Failed to send data with status code: ");
    Serial.println(response.statusCode);
  }
  client->flushResponse(response);
}

bool isMandatoryParametersMissing(Config* configs){
  bool isMissing = false;

  const char* mandatoryParameters[] = {"wifi_ssid", "wifi_password", "username", "password", "host"};
  for(int i=0; i<sizeof(mandatoryParameters)/sizeof(const char*); i++){
    if(!configs->has(mandatoryParameters[i])){
      isMissing = true;
      Serial.print("Parameter is missing: ");
      Serial.println(mandatoryParameters[i]);
    }
  }
  return isMissing;
}

void executeProcedure(){
  Serial.println(ESP.getFreeHeap());

  Config* config = new Config();
  config->loadConfig();

  if(isMandatoryParametersMissing(config)){
    return;
  }

  Serial.print("wifi_ssid:");
  Serial.println(config->get("wifi_ssid"));
  Serial.print("wifi_password:");
  Serial.println(config->get("wifi_password"));
  Serial.print("username:");
  Serial.println(config->get("username"));
  Serial.print("password:");
  Serial.println(config->get("password"));
  Serial.print("host:");
  Serial.println(config->get("host"));
  Serial.print("sleep_time:");
  Serial.println(config->getInt("sleep_time"));
  
  client = new RestClient(config->get("wifi_ssid"), config->get("wifi_password"), config->get("host"));
  client->withBasicAuthentication(config->get("username"), config->get("password"));
  client->setup();
  postMeasurements();

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
