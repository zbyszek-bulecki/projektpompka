import { Component, OnInit } from '@angular/core';
import { UserInfoService } from '../services/user-info.service';

@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.css']
})
export class NavigationBarComponent implements OnInit {

  constructor(private userInfoService: UserInfoService) { }

  ngOnInit(): void {
  }

  logout(){
    this.userInfoService.logout();
  }
}
