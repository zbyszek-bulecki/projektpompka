export class Device{
    adresMac: String;
    status: String;
    lastUpdate: String;

    constructor(adresMac: String, status: String, lastUpdate: String){
        this.adresMac = adresMac;
        this.status = status;
        this.lastUpdate = lastUpdate;
    }
}