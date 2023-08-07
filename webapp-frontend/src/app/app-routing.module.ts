import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DeviceStatusComponent } from './device-status/device-status.component';
import { DevicesListComponent } from './devices-list/devices-list.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { UserInfoService } from './services/user-info.service';
import { AnonymousUserService } from './services/anonymous-user.service';

const routes: Routes = [
  { path: 'device', component: DeviceStatusComponent, canActivate: [UserInfoService] },
  { path: 'devices', component: DevicesListComponent, canActivate: [UserInfoService] },
  { path: '**', component: LoginPageComponent, canActivate: [AnonymousUserService] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
