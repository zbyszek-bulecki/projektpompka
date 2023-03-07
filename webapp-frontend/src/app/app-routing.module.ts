import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DeviceStatusComponent } from './device-status/device-status.component';
import { DevicesListComponent } from './devices-list/devices-list.component';

const routes: Routes = [
  { path: 'status', component: DeviceStatusComponent },
  { path: '**', component: DevicesListComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
