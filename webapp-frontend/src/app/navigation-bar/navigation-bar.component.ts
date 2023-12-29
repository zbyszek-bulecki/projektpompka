import { Component, OnInit } from '@angular/core';
import { UserInfoService } from '../services/user-info.service';

@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.css']
})
export class NavigationBarComponent implements OnInit {

  username: string | null = null;

  constructor(private userInfoService: UserInfoService) { 
    this.username = userInfoService.userInfo!.username;
  }

  ngOnInit(): void {}

  logout(){
    this.userInfoService.logout();
  }
}
