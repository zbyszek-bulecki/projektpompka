#include <Arduino.h>
#include "Config.h"
#include "RestClient.h"
#include "newSensors.h"
#define SETTINGS_LAST_UPDATED_TIMESTAMP "settings_last_updated_timestamp"

RestClient *client;

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

Measurements getMeasurements()
{
  SensorsPompka *sensors = new SensorsPompka();
  Measurements measurements = sensors->getMeasurements();
  free(sensors);
  return measurements;
}

void postMeasurements(char *deviceName, Measurements measurements)
{

  Serial.println("***********POST***********");
  DynamicJsonDocument *request = new DynamicJsonDocument(REST_PAYLOAD_SIZE);

  (*request)["name"] = deviceName;
  (*request)["macAddress"] = client->getMacAddress();
  (*request)["soilMoisture"] = measurements.soilMoisture;
  (*request)["lightIntensity"] = measurements.lightIntensity;
  (*request)["temperature"] = measurements.temperature;
  (*request)["pressure"] = measurements.pressure;
  (*request)["waterLevel"] = measurements.waterLevel;

  Serial.println("Sending...");
  Response response = client->sendPost("/planter/measurements", request);
  delete request;

  if (response.statusCode == 200)
  {
    Serial.println("Data send successfully!");
  }
  else
  {
    Serial.print("Failed to send data with status code: ");
    Serial.println(response.statusCode);
  }
  client->flushResponse(response);
}

void fetchSettings(Config *config)
{
  DynamicJsonDocument *request = new DynamicJsonDocument(REST_PAYLOAD_SIZE);

  (*request)["name"] = config->get("device_name");
  (*request)["macAddress"] = client->getMacAddress();
  if (config->has(SETTINGS_LAST_UPDATED_TIMESTAMP))
  {
    (*request)["timestamp"] = config->get(SETTINGS_LAST_UPDATED_TIMESTAMP);
  }

  Serial.println("Sending...");
  Response response = client->sendPost("/planter/settings", request);
  delete request;

  if (response.statusCode == 200)
  {
    const char *timestamp = (*response.payload)["timestamp"];
    Serial.print("timestamp: ");
    Serial.println(timestamp);
    config->set(SETTINGS_LAST_UPDATED_TIMESTAMP, timestamp);

    JsonObject settings = (*response.payload)["settings"].as<JsonObject>();

    for (JsonPair kv : settings)
    {
      Serial.print("Key: ");
      Serial.print(kv.key().c_str());
      Serial.print(", Value: ");
      Serial.println(kv.value().as<String>());
      config->set(kv.key().c_str(), kv.value().as<String>().c_str());
    }
    config->saveConfigFile();
  }
  else
  {
    Serial.print("Failed to send data with status code: ");
    Serial.println(response.statusCode);
  }
  client->flushResponse(response);
}

bool isMandatoryParametersMissing(Config *configs)
{
  bool isMissing = false;

  const char *mandatoryParameters[] = {"wifi_ssid", "wifi_password", "username", "password", "host", "device_name"};
  for (int i = 0; i < sizeof(mandatoryParameters) / sizeof(const char *); i++)
  {
    if (!configs->has(mandatoryParameters[i]))
    {
      isMissing = true;
      Serial.print("Parameter is missing: ");
      Serial.println(mandatoryParameters[i]);
    }
  }
  return isMissing;
}

void executeProcedure()
{
  Config *config = new Config();
  config->loadConfigFile();

  if (isMandatoryParametersMissing(config))
  {
    return;
  }

  Serial.print("wifi_ssid:");
  Serial.println(config->get("wifi_ssid"));
  Serial.print("wifi_password:");
  Serial.println(config->get("wifi_password"));
  Serial.print("device_name:");
  Serial.println(config->get("device_name"));
  Serial.print("username:");
  Serial.println(config->get("username"));
  Serial.print("password:");
  Serial.println(config->get("password"));
  Serial.print("host:");
  Serial.println(config->get("host"));
  Serial.print("sleep_time:");
  Serial.println(config->getInt("sleep_time"));

  Measurements measurements = getMeasurements();

  client = new RestClient(config->get("wifi_ssid"), config->get("wifi_password"), config->get("host"));
  client->withBasicAuthentication(config->get("username"), config->get("password"));
  client->setup();
  postMeasurements(config->get("device_name"), measurements);
  fetchSettings(config);

  delete client;
  delete config;

  sleep(5);
}

void setup()
{
  Serial.begin(115200);
  sleep(2);
  executeProcedure();
  Serial.println("");
}

void loop()
{
  Serial.print(".");
  sleep(5);
}
