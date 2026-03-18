import { Component, Input, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReportInput } from '../../../types/report';
import { CommonModule } from '@angular/common';
import { ReportService } from '../../../core/report/report-service';

@Component({
  selector: 'app-report',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './report.html',
  styleUrls: ['./report.scss'],
})
export class Report  {

  constructor(private reportService: ReportService){}

  reportReason = signal<string | null>(null);

  @Input() postId!: number;
  @Input() reporterId!: number;


  reportForm: FormGroup<{
    // postId: FormControl<number |  null>;
    // reporterId: FormControl<number | null>;
    reason: FormControl<string | null>;
    details: FormControl<string | null>;
  }> = new FormGroup({
    // postId: new FormControl(0, Validators.required),   // placeholder, will update in ngOnChanges
    // reporterId: new FormControl(0, Validators.required),
    reason: new FormControl('', Validators.required),
    details: new FormControl('', Validators.required)
  });

  // ngOnChanges(changes: SimpleChanges) {
  //   // Update values dynamically when inputs change
  //   if (changes['postId'] && this.postId != null) {
  //     this.reportForm.get('postId')?.setValue(Number(this.postId));
  //   }

  //   if (changes['reporterId'] && this.reporterId != null) {
  //     this.reportForm.get('reporterId')?.setValue(Number(this.reporterId));
  //   }
  // }


  onSubmit() {
    if (this.reportForm.valid) {
      const report: ReportInput = {
        postId: this.postId,
        reporterId: this.reporterId,
        reason: this.reportForm.value.reason! == "other" ? this.reportForm.value.details! :  this.reportForm.value.reason!
      }
     
      this.reportService.postReport(report).subscribe(res =>  console.log('Submitted Report:', res))
    } else {
      console.log('Form invalid');
      this.reportForm.markAllAsTouched();
    }
  }
}
