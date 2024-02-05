import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DevicePreviewDataService {
  selected: {
    name: string | null,
    macAddress: string | null
  } = {
    name: null,
    macAddress: null
  }
}
