import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileAdmin } from './profile-admin';

describe('ProfileAdmin', () => {
  let component: ProfileAdmin;
  let fixture: ComponentFixture<ProfileAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
