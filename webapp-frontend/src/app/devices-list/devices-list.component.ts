import { Component, OnInit, SimpleChanges } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Device } from 'src/domain/device';

@Component({
  selector: 'app-devices-list',
  templateUrl: './devices-list.component.html',
  styleUrls: ['./devices-list.component.css']
})
export class DevicesListComponent implements OnInit {

  deviceList: Device[] = [];

  text = "some text";

  constructor(private httpClient: HttpClient) { }

  ngOnInit(): void {
    this.getDevicesList();
  }

  ngOnChanges(changes: SimpleChanges) {
    console.log("some change");
  }

  onClick(){
    this.getDevicesList();
  }

  handleInput(event: Event | null) {
    if(event===null) return;
    this.text = (event.target as HTMLInputElement).value;
  }

  getDevicesList(){
    this.httpClient.get<Device[]>("/data/devices-list").subscribe(data => {
      this.deviceList = data;
    });        
  }
}
