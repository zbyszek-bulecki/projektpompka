import { Component, OnInit, SimpleChanges } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http'
import { Device } from 'src/domain/device';
import { UserInfoService } from '../services/user-info.service';
import { RestClientService } from '../services/rest-client.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { PageableTableComponent, PageableTableConfig, Table, TableColumn, TableHeader, TableRow } from '../pageable-table/pageable-table.component';

@Component({
  selector: 'app-devices-list',
  templateUrl: './devices-list.component.html',
  styleUrls: ['./devices-list.component.css']
})
export class DevicesListComponent implements OnInit {

  constructor(private userInfoService: UserInfoService, private restClient: RestClientService, private router: Router, private route: ActivatedRoute) {
  }

  tableConfig = new PageableTableConfig();
  table: Table | null = null;

  ngOnInit(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      const pageParam = params.get('page');
      const pageSizeParam = params.get('pageSize');
      this.tableConfig.page = pageParam ? +pageParam : this.tableConfig.page;
      this.tableConfig.pageSize = pageSizeParam ? +pageSizeParam : PageableTableComponent.getDefaultPageSize();
      this.tableConfig.noElementsMessage = "No devices found.";
      this.loadDevices(this.tableConfig.page, this.tableConfig.pageSize);
    });
  }
  
  loadDevices(page: number, size: number){
    const requestParams = new HttpParams().set("page", page).set("size", size);    
    this.restClient.get("/manager/planters", requestParams).subscribe(response =>{
      this.setupTable(response.body.content);
      this.tableConfig.page = response.body.page;
      this.tableConfig.totalElements = response.body.totalElements;   
    });
  }

  openPage(page: number){
    if(this.tableConfig.pageSize!=PageableTableComponent.getDefaultPageSize()){
      this.router.navigate(['/devices', page, this.tableConfig.pageSize]);
    }
    else{
      this.router.navigate(['/devices', page]);
    }
  }

  setupTable(devices: [
    {
        name: string,
        macAddress: string,
        lastActivity: string,
        soilMoisture: number,
        lightIntensity: number,
        temperature: number,
        pressure: number,
        waterLevel: number
    }
  ]){
    let header = TableHeader.of(["name","mac address","last activity","soil moisture","light intensity","temperature","pressure","water level",""]);

    let rows: TableRow[] = [];
    for(let r of devices){
      rows.push(TableRow.of([r.name, r.macAddress, r.lastActivity, r.soilMoisture.toString(), 
        r.lightIntensity.toString(), r.temperature.toString(), r.pressure.toString(), 
        r.waterLevel.toString(), {value: 'details', link:['/device',r.name,r.macAddress]}]));
    }
  
    this.table = new Table(header, rows);
  }
}
