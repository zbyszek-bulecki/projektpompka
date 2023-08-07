import { TestBed } from '@angular/core/testing';

import { AnonymousUserService } from './anonymous-user.service';

describe('AnonymousUserService', () => {
  let service: AnonymousUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnonymousUserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
