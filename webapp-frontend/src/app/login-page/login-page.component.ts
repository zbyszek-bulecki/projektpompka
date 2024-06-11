import { Component, OnInit } from '@angular/core';
import { UserInfoService } from '../services/user-info.service';
import { RestClientService } from '../services/rest-client.service';
import { HttpStatusCode } from '@angular/common/http';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class LoginPageComponent implements OnInit {

  error: string | null = null;

  credentialsForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  })

  constructor(private userInfoService: UserInfoService, private router: Router) { }

  ngOnInit(): void {
  }

  isFailedAttempt(): boolean{
    return !(this.error===null);
  }

  performLoginAttempt(){
    this.userInfoService.login({
      username : this.credentialsForm.value.username ?? '',
      password : this.credentialsForm.value.password ?? ''
    });
  }
}