function ContentManager(){
    this.filters = [];

    this.addFilter=function(filter){
        this.filters.push(filter);
    };

    this.buildRequest=function(url, successCallback){
        return new Request(url, successCallback).withFilters(this.filters);
    };
    
    this.buildRequestComponent=function(url, selector){    
        return new Request(url, response=>{setContent(selector, response.body)}).withFilters(this.filters).withResponseTypeText();
    };
}

function Request(url, successCallback){
    this.method = "GET"
    this.url = url;
    this.expectedStatusCode = 200;    
    this.responseType = "JSON";
    this.successCallback = successCallback;
    this.errorCallback = undefined;
    this.body = undefined;
    this.filters = [];

    this.get=function(){
      this.method = "GET";
      return this;
    };
    this.post=function(){
        this.method = "POST";
        return this;
    };
    this.put=function(){
        this.method = "PUT";
        return this;
    };
    this.patch=function(){
        this.method = "PATCH";
        return this;
    };
    this.delete=function(){
        this.method = "DELETE";
        return this;
    };

    this.withResponseTypeText=function(){
        this.responseType = "TEXT";
        return this;
    };

    this.withResponseTypeJSON=function(){
        this.responseType = "JSON";
        return this;
    };

    this.withExpectedStatusCode=function(expectedStatusCode){
        this.expectedStatusCode = expectedStatusCode;
        return this;
    };

    this.withErrorCallback=function(errorCallback){
        this.errorCallback = errorCallback;
        return this;
    };

    this.withBody=function(body){
        this.body = body;
        return this;
    };

    this.withFilters=function(filters){
        this.filters = filters;
        return this;
    };

    this.send=function(){
        request(this.method, this.url, this.expectedStatusCode, this.responseType, this.successCallback, this.errorCallback, this.body, this.filters);
    };
}

function request(method, url, expectedStatusCode, responseType, successCallback, errorCallback, body, filters){
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange=function() {        
        if (xhttp.readyState === XMLHttpRequest.DONE){
            try {
                let response = new Object();
                switch(responseType){
                    case "JSON":
                        response.body = JSON.parse(xhttp.responseText);
                        break;
                    case "TEXT":
                        response.body = xhttp.responseText;
                        break;
                };
                response.status = xhttp.status;
                if(handleFilters(response, filters)){
                    if(response.status === expectedStatusCode){
                        executeCallback(successCallback, response);
                    }
                    else{
                        executeCallback(errorCallback, response);
                    }
                }
            } catch (error) {
                executeCallback(errorCallback, undefined);
            }
        } 
    }
    xhttp.open(method, url, true);
    if(['GET', 'DELETE'].includes(method)){
        xhttp.send();
    }
    else{
        xhttp.send(body);
    }
}

function handleFilters(response, filters){
    for(const filter of filters){
        if(!filter(response)){
            return false;
        }
    }
    return true;
}

function executeCallback(callback, data){
    if(callback != undefined){
        callback(data);
    }
}

function setContent(selector, html) {
    let container = document.querySelector(selector);
    var newElements = createElements(html);
    container.innerHTML = "";
    Array.from(newElements.childNodes).forEach(function(element){
        container.appendChild(element);
    });
};
function copyNode(node){
    const copiedNode = document.createElement(node.nodeName);	
    Array.from(node.attributes).forEach(function(attribute){
        copiedNode.setAttribute(attribute.name, attribute.value)
    });
    copiedNode.innerHTML = node.innerHTML;
    return copiedNode;
};
function createElements(html){
    var newElements = document.createElement("tmp");
    newElements.innerHTML = html;
    Array.from(newElements.querySelectorAll("script")).forEach(function(script){
        script.parentNode.replaceChild(copyNode(script), script);
    });
    return newElements;
};

var contentManager = new ContentManager();