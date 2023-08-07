import { Component, OnInit } from '@angular/core';
import { UserInfoService } from '../services/user-info.service';
import { RestClientService } from '../services/rest-client.service';
import { HttpStatusCode } from '@angular/common/http';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {

  error: string | null = null;

  credentials: {
    username: string,
    password: string
  } = {
    username: '',
    password: ''
  }

  constructor(private userInfoService: UserInfoService, private restClient: RestClientService, private router: Router) { }

  ngOnInit(): void {
  }

  isFailedAttempt(): boolean{
    return !(this.error===null);
  }

  performLoginAttempt(){
    this.restClient.post('auth/login', this.credentials).subscribe(response => {
      if(response.status === HttpStatusCode.Ok){
        this.userInfoService.requestUserInfo().subscribe(data => this.router.navigate(["/devices"]));
      }
    });
  }
}
