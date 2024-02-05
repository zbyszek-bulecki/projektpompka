import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { RestClientService } from '../services/rest-client.service';
import {Location} from '@angular/common'; 
import { DevicePreviewDataService } from '../services/device-preview-data.service';

@Component({
  selector: 'app-device-preview',
  templateUrl: './device-preview.component.html',
  styleUrls: ['./device-preview.component.css']
})
export class DevicePreviewComponent implements OnInit{

  constructor(private restClient: RestClientService, private route: ActivatedRoute, private location: Location, private devicePreviewData: DevicePreviewDataService) { }

  name: string | null = null;
  macAddress: string | null = null;
  lastActivity: string | null = null;

  ngOnInit(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.name = params.get('name');
      this.macAddress = params.get('mac');
      this.devicePreviewData.selected.name = this.name;
      this.devicePreviewData.selected.macAddress = this.macAddress;
      this.loadInfo();
    });
  }

  loadInfo(){  
    this.restClient.get("/manager/planters/"+this.name+"/"+this.macAddress).subscribe(response =>{
      this.lastActivity = response.body.lastActivity;
    });
  }

  onActivate(componentReference: any) {
    
 }

}
