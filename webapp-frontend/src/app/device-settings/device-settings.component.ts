import { Component } from '@angular/core';
import { DevicePreviewDataService } from '../services/device-preview-data.service';

@Component({
  selector: 'app-device-settings',
  templateUrl: './device-settings.component.html',
  styleUrls: ['./device-settings.component.css']
})
export class DeviceSettingsComponent {

  constructor (private devicePreviewData: DevicePreviewDataService) {
    
  }

}
