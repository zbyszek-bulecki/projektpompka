import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageableTableComponent } from './pageable-table.component';

describe('PageableTableComponent', () => {
  let component: PageableTableComponent;
  let fixture: ComponentFixture<PageableTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PageableTableComponent]
    });
    fixture = TestBed.createComponent(PageableTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
