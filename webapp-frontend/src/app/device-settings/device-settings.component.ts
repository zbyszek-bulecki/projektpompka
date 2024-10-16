import { Component } from '@angular/core';
import { DevicePreviewDataService } from '../services/device-preview-data.service';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestClientService } from '../services/rest-client.service';

@Component({
  selector: 'app-device-settings',
  templateUrl: './device-settings.component.html',
  styleUrls: ['./device-settings.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
})
export class DeviceSettingsComponent {
  constructor(
    private devicePreviewData: DevicePreviewDataService,
    private restClient: RestClientService,
    private formBuilder: FormBuilder
  ) {}

  macAddress: string | null = null;
  name: string | null = null;
  settingsList: SettingRow[] | null = [];

  settingsForm: FormGroup | null = null;

  ngOnInit(): void {
    this.name = this.devicePreviewData.selected.name;
    this.macAddress = this.devicePreviewData.selected.macAddress;
    this.loadSettings();
  }

  loadSettings() {
    this.restClient
      .get<SettingRow[]>(
        '/manager/planters/' + this.name + '/' + this.macAddress + '/settings'
      )
      .subscribe((response) => {
        this.settingsList = response.body;
        console.log('test');
        console.log(this.settingsList);
        this.createForm();
      });
  }

  createForm() {
    if (this.settingsList != null) {
      const formGroup: Record<string, FormControl> = {};
      this.settingsList.forEach((s) => console.log(s as SettingRow));
      this.settingsList.forEach((s) => {
        formGroup[s.key] = new FormControl(s.value);
        formGroup[s.key + '_default'] = new FormControl(s.isDefault);
      });
      this.settingsForm = this.formBuilder.group(formGroup);
    }
  }

  submitSettings() {
    if (this.settingsForm instanceof FormGroup) {
      console.log(this.groupFields(this.settingsForm.value));
    }
  }
  groupFields(obj: any) {
    return Object.entries(obj).reduce((acc: any, [key, value]) => {
      // Sprawdź, czy pole to 'nazwa' czy 'nazwa_default'
      if (key.endsWith('_default')) {
        const baseKey = key.replace('_default', '');

        // Jeśli grupa już istnieje, przypisz 'default'
        if (!acc[baseKey]) {
          acc[baseKey] = { value: null, resetToDefault: value };
        } else {
          acc[baseKey].resetToDefault = value;
        }
      } else {
        // Pole zwykłe (bez _default)
        const baseKey = key;

        if (!acc[baseKey]) {
          acc[baseKey] = { value: value, resetToDefault: null };
        } else {
          acc[baseKey].value = value;
        }
      }
      return acc;
    }, {});
  }

  getIsDefaultCheck(label: string): boolean {
    if (label.endsWith('_default')) label = label.replace('_default', '');
    console.log(this.settingsForm?.value[label + '_default']);
    return this.settingsForm?.value[label + '_default'];
  }
}

class SettingRow {
  public key: string;
  public value: string;
  public isDefault: boolean;
  constructor(key: string, value: string, isDefault: boolean) {
    this.key = key;
    this.value = value;
    this.isDefault = isDefault;
  }
}
