import { Component, OnInit, SimpleChanges } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Device } from 'src/domain/device';
import { UserInfoService } from '../services/user-info.service';

@Component({
  selector: 'app-devices-list',
  templateUrl: './devices-list.component.html',
  styleUrls: ['./devices-list.component.css']
})
export class DevicesListComponent implements OnInit {

  username: string;

  constructor(private httpClient: HttpClient, private userInfoService: UserInfoService) {
    this.username = userInfoService.userInfo!.username;
  }


  ngOnInit(): void {

  }
  
}
