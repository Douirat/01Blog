import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Registeration } from './registeration';

describe('Registeration', () => {
  let component: Registeration;
  let fixture: ComponentFixture<Registeration>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Registeration]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Registeration);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
