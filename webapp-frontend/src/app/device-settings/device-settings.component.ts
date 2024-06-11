import { Component } from '@angular/core';
import { DevicePreviewDataService } from '../services/device-preview-data.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-device-settings',
  templateUrl: './device-settings.component.html',
  styleUrls: ['./device-settings.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class DeviceSettingsComponent {

  constructor (private devicePreviewData: DevicePreviewDataService) {
    
  }

  settingsForm = new FormGroup({
    ssid: new FormControl(''),
    password: new FormControl(''),
    host: new FormControl (''),
    sleepTime: new FormControl('')
  })

  submitSettings() {
    console.log(
      this.settingsForm.value.ssid ?? '',
      this.settingsForm.value.password ?? '',
      this.settingsForm.value.host ?? '',
      this.settingsForm.value.sleepTime ?? ''
    )
  }

}
