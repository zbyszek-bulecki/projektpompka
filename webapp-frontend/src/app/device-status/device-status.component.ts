import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { PageableTableComponent, PageableTableConfig, Table, TableHeader, TableRow } from '../pageable-table/pageable-table.component';
import { HttpParams } from '@angular/common/http';
import { RestClientService } from '../services/rest-client.service';
import {Location} from '@angular/common'; 

@Component({
  selector: 'app-device-status',
  templateUrl: './device-status.component.html',
  styleUrls: ['./device-status.component.css']
})
export class DeviceStatusComponent implements OnInit {

  constructor(private restClient: RestClientService, private route: ActivatedRoute, private location: Location) { }

  name: string | null = null;
  macAddress: string | null = null;
  lastActivity: string | null = null;

  tableConfig = new PageableTableConfig();
  table: Table | null = null;

  ngOnInit(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.name = params.get('name');
      this.macAddress = params.get('mac');
      const pageParam = params.get('page');
      const pageSizeParam = params.get('pageSize');
      this.tableConfig.page = pageParam ? +pageParam : this.tableConfig.page;
      this.tableConfig.pageSize = pageSizeParam ? +pageSizeParam : PageableTableComponent.getDefaultPageSize();
      this.loadInfo();
      this.loadMeasurements(this.tableConfig.page, this.tableConfig.pageSize);
    });
  }

  loadInfo(){  
    this.restClient.get("/manager/planters/"+this.name+"/"+this.macAddress).subscribe(response =>{
      this.lastActivity = response.body.lastActivity;
    });
  }

  loadMeasurements(page: number, size: number){
    const requestParams = new HttpParams().set("page", page).set("size", size);    
    this.restClient.get("/manager/planters/"+this.name+"/"+this.macAddress+"/measurements", requestParams).subscribe(response =>{
      this.setupTable(response.body.content);
      console.log(response);
      this.tableConfig.page = response.body.page;
      this.tableConfig.totalElements = response.body.totalElements;   
    });
  }

  openPage(page: number){
    this.loadMeasurements(page, this.tableConfig.pageSize);
    if(this.tableConfig.pageSize!=PageableTableComponent.getDefaultPageSize()){
      this.location.replaceState(['/device', this.name, this.macAddress, page, this.tableConfig.pageSize].join("/"));      
    }
    else{
      this.location.replaceState(['/device', this.name, this.macAddress, page].join("/"));
    }
  }

  setupTable(devices: [
    {
        soilMoisture: number,
        lightIntensity: number,
        temperature: number,
        pressure: number,
        waterLevel: number,
        createdAt: string
    }
  ]){
    let header = TableHeader.of(["soil moisture","light intensity","temperature","pressure","water level","created at"]);

    let rows: TableRow[] = [];
    for(let r of devices){
      rows.push(
        TableRow.of([r.soilMoisture.toString(), r.lightIntensity.toString(), r.temperature.toString(), r.pressure.toString(), 
          r.waterLevel.toString(), r.createdAt]));
    }
  
    this.table = new Table(header, rows);
  }

}