export class Device{
    adresMac: String;
    status: String;
    lastActivity: String;

    constructor(adresMac: String, status: String, lastActivity: String){
        this.adresMac = adresMac;
        this.status = status;
        this.lastActivity = lastActivity;
    }
}