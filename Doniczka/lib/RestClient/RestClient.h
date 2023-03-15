#pragma once
#include "ArduinoJson.h"
#include "WiFi.h"
#include "HTTPClient.h"

#define REST_PAYLOAD_SIZE 8000

#define REST_GET 0
#define REST_POST 1
#define REST_PATCH 2
#define REST_PUT 3
#define REST_DELETE 4

struct Response{    
    int statusCode;
    DynamicJsonDocument* payload;

};

class RestClient{
    const char* ssid;
    const char* password;
    String serverUrl;

    bool active = false;

    void logRequest(String url, Response response);
    DynamicJsonDocument* deserializeResponsePayload(String rawPayload);
    Response sendRequest(int method, String url, DynamicJsonDocument* body);
    
    public:
    RestClient(char* ssid, char* password, char* serverUrl);
    void setup();
    Response sendGet(String url);
    Response sendDelete(String url);
    Response sendPost(String url, DynamicJsonDocument* body);
    Response sendPatch(String url, DynamicJsonDocument* body);
    Response sendPut(String url, DynamicJsonDocument* body);
    void flushResponse(Response response);
    void disconnect();
};