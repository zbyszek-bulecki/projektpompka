import { Component } from '@angular/core';
import { DevicePreviewDataService } from '../services/device-preview-data.service';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
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
    private formBuilder : FormBuilder
  ) { }

  macAddress: string | null = null;
  name: string | null = null;
  settingsList: SettingRow[] | null = [];

  settingsForm : FormGroup | null = null;

  ngOnInit(): void {
    this.name = this.devicePreviewData.selected.name;
    this.macAddress = this.devicePreviewData.selected.macAddress;
    this.loadSettings();
  }

  loadSettings() { 
    this.restClient.get<SettingRow[]>("/manager/planters/" + this.name + "/" + this.macAddress + "/settings").subscribe(response =>{
      this.settingsList = response.body;
      console.log("test");
      console.log(this.settingsList);
      this.createForm();
    });
  }

  // createFormControl(settingRow : SettingRow) {

  // }

  createForm() {
    if(this.settingsList != null) {
      const formGroup : Record <string, FormControl> = {};
      this.settingsList.forEach(s => console.log(s as SettingRow));
      this.settingsList.forEach(s => formGroup[s.getKey()] = new FormControl(s.getValue()));
      this.settingsForm = this.formBuilder.group(formGroup);
      console.log("Cokolwiek");
    }
    console.log("Coś innego");
  }


  submitSettings() {
    if(this.settingsForm instanceof FormGroup)
      {console.log(
        this.settingsForm.value
    );
  }
    }

}

class SettingRow {
  constructor (
    private key: string,
    private value: string,
    private isDefault: Boolean
  ) { }

  getKey(): string{
    return this.key;
  }

  getValue(): string{
    return this.value;
  }

  getIsDefault(): Boolean{
    return this.isDefault;
  }
}
