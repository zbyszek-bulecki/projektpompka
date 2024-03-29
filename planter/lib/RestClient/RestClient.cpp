#include "RestClient.h"

RestClient::RestClient(char* ssid, char* password, char* serverUrl){
    this->wifiSsid = ssid;
    this->wifiPassword = password;
    this->serverUrl = String(serverUrl);
    useBasicAuthentiction = false;
    if(!this->serverUrl.startsWith("http://")){
        this->serverUrl = String("http://") + this->serverUrl;
    }
}

void RestClient::withBasicAuthentication(char* username, char* password){
    useBasicAuthentiction = true;
    this->username = username;
    this->password = password;
}

void RestClient::setup(){
    this->active = true;

    WiFi.begin(this->wifiSsid, this->wifiPassword);
    Serial.println("Connecting");
    while(WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
    Serial.println("");
    Serial.print("Connected to WiFi network with IP Address: ");
    Serial.println(WiFi.localIP());
}

Response RestClient::sendGet(String path){
    return sendRequest(REST_GET, path, NULL);
}

Response RestClient::sendDelete(String path){
    return sendRequest(REST_DELETE, path, NULL);
}

Response RestClient::sendPost(String path, DynamicJsonDocument* body){
    return sendRequest(REST_POST, path, body);
}

Response RestClient::sendPatch(String path, DynamicJsonDocument* body){
    return sendRequest(REST_PATCH, path, body);
}

Response RestClient::sendPut(String path, DynamicJsonDocument* body){
    return sendRequest(REST_PUT, path, body);
}

Response RestClient::sendRequest(int method, String path, DynamicJsonDocument* body){
    Response response;
    if(WiFi.status()!=WL_CONNECTED && this->active){
        this->setup();
    }

    if(WiFi.status()==WL_CONNECTED){
        HTTPClient http;
        String serverPath = this->serverUrl + path;

        http.begin(serverPath);

        if(useBasicAuthentiction){
            addCredentials(http);
        }
        
        String payload;
        switch (method){
            case REST_POST:
            case REST_PATCH:
            case REST_PUT:
                http.addHeader("Content-Type", "application/json");
                serializeJson(*body, payload);
                Serial.println(payload);
                break;            
            default:
                break;
        }
        
        switch (method){
            case REST_GET:
                response.statusCode = http.GET();
                break;
            case REST_DELETE:
                response.statusCode = http.sendRequest("DELETE");
                break;  
            case REST_POST:
                response.statusCode = http.POST(payload);
                break; 
            case REST_PATCH:
                response.statusCode = http.PATCH(payload);
                break; 
            case REST_PUT:
                response.statusCode = http.PUT(payload);
                break;            
            default:
                break;
        }
        String responseBody = http.getString();
        Serial.print(responseBody);
        response.payload = deserializeResponsePayload(responseBody);
        
        logRequest(serverPath, response);
        http.end();
    }
    else {
      Serial.println("WiFi Disconnected");
    }
    return response;
}

void RestClient::addCredentials(HTTPClient& http){
    String user = this->username;
    String credentials = base64::encode(user + ":" + this->password);
    http.addHeader("Authorization", "Basic " + credentials);
}

void RestClient::logRequest(String url, Response response){
    Serial.print("HTTP Response [");
    Serial.print(url);
    Serial.print("]: ");
    Serial.println(response.statusCode);
}

DynamicJsonDocument* RestClient::deserializeResponsePayload(String rawResponse){
    Serial.println(rawResponse);
    DynamicJsonDocument* doc = new DynamicJsonDocument(REST_PAYLOAD_SIZE);
    if(rawResponse.length()==0){
        return doc;
    }
    DeserializationError error = deserializeJson(*doc, rawResponse);
    if(error){
        Serial.print("Couldn't parse: ");
        Serial.println(rawResponse);
    }
    return doc;
}

void RestClient::flushResponse(Response response){
    delete response.payload;
}

void RestClient::disconnect(){
    this->active = false;
    WiFi.disconnect();
}