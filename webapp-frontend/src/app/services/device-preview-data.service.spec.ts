import { TestBed } from '@angular/core/testing';

import { DevicePreviewDataService } from './device-preview-data.service';

describe('DevicePreviewDataService', () => {
  let service: DevicePreviewDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DevicePreviewDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
