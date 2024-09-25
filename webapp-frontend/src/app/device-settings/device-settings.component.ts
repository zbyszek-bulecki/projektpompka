import { Component } from '@angular/core';
import { DevicePreviewDataService } from '../services/device-preview-data.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestClientService } from '../services/rest-client.service';

@Component({
  selector: 'app-device-settings',
  templateUrl: './device-settings.component.html',
  styleUrls: ['./device-settings.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class DeviceSettingsComponent {

  constructor (
    private devicePreviewData: DevicePreviewDataService,
    private restClient: RestClientService,
  ) { }

  macAddress: string | null = null;
  name: string | null = null;
  settingsList: SettingRow[] = [];

  ngOnInit(): void {
    this.name = this.devicePreviewData.selected.name;
    this.macAddress = this.devicePreviewData.selected.macAddress;
    this.loadSettings();
  }

  loadSettings() { 
    this.restClient.get<SettingRow[]>("/manager/planters/" + this.name + "/" + this.macAddress + "/settings").subscribe(response =>{
      this.settingsList = response;
      console.log("test");
      console.log(this.settingsList);
    });
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

class SettingRow {
  constructor (
    private key: String,
    private value: String,
    private isDefault: Boolean
  ) { }

  getKey(): String{
    return this.key;
  }

  getValue(): String{
    return this.value;
  }

  getIsDefault(): Boolean{
    return this.isDefault;
  }
}
