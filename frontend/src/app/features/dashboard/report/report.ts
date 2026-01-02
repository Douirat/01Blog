import { Component, Input, OnChanges, SimpleChanges, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReportInput } from '../../../types/report';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-report',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './report.html',
  styleUrls: ['./report.scss'],
})
export class Report implements OnChanges {

  reportReason = signal<string | null>(null);

  @Input() postId!: number;
  @Input() reporterId!: number;


  reportForm: FormGroup<{
    postId: FormControl<number | null>;
    reporterId: FormControl<number | null>;
    reason: FormControl<string | null>;
  }> = new FormGroup({
    postId: new FormControl(0, Validators.required),   // placeholder, will update in ngOnChanges
    reporterId: new FormControl(0, Validators.required),
    reason: new FormControl('', Validators.required)
  });

  ngOnChanges(changes: SimpleChanges) {
    // Update values dynamically when inputs change
    if (changes['postId'] && this.postId != null) {
      this.reportForm.get('postId')?.setValue(Number(this.postId));
    }

    if (changes['reporterId'] && this.reporterId != null) {
      this.reportForm.get('reporterId')?.setValue(Number(this.reporterId));
    }
  }

  onSubmit() {
    if (this.reportForm.valid) {
      const value: ReportInput = this.reportForm.getRawValue(); // plain object
      console.log('Submitted Report:', value);
    } else {
      console.log('Form invalid');
      this.reportForm.markAllAsTouched();
    }
  }
}
