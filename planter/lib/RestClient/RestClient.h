#pragma once
#include "ArduinoJson.h"
#include <Base64.h>
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
    const char* wifiSsid;
    const char* wifiPassword;
    String serverUrl;
    bool useBasicAuthentiction;
    char* username;
    char* password;

    bool active = false;

    void logRequest(String url, Response response);
    DynamicJsonDocument* deserializeResponsePayload(String rawPayload);
    Response sendRequest(int method, String url, DynamicJsonDocument* body);
    void addCredentials(HTTPClient& http);
    
    public:
    RestClient(char* wifiSsid, char* wifiPassword, char* serverUrl);
    void withBasicAuthentication(char* username, char* password);
    void setup();
    Response sendGet(String url);
    Response sendDelete(String url);
    Response sendPost(String url, DynamicJsonDocument* body);
    Response sendPatch(String url, DynamicJsonDocument* body);
    Response sendPut(String url, DynamicJsonDocument* body);
    void flushResponse(Response response);
    void disconnect();
};