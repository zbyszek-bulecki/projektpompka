import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DeviceStatusComponent } from './device-status/device-status.component';
import { DevicesListComponent } from './devices-list/devices-list.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { UserInfoService } from './services/user-info.service';
import { AnonymousUserService } from './services/anonymous-user.service';
import { HomePageComponent } from './home-page/home-page.component';

const routes: Routes = [
  { path: 'login', component: LoginPageComponent, canActivate: [AnonymousUserService] },
  { path: '', component: HomePageComponent, canActivate: [UserInfoService], children: [
    { path: 'device/:name/:mac', component: DeviceStatusComponent, children: [
      { path: '', component: DeviceStatusComponent},
      { path: 'status', component: DeviceStatusComponent},
      { path: 'status/:page', component: DeviceStatusComponent},
      { path: 'status/:page/:pageSize', component: DeviceStatusComponent},
    ]},
    { path: '', component: DevicesListComponent},
    { path: 'devices', component: DevicesListComponent},
    { path: 'devices/:page', component: DevicesListComponent},
    { path: 'devices/:page/:pageSize', component: DevicesListComponent}
  ]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
