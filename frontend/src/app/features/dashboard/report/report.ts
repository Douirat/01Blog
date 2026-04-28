import { Component, Input, OnInit, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReportInput } from '../../../types/report';
import { CommonModule } from '@angular/common';
import { ReportService } from '../../../core/report/report-service';
import { ToastService } from '../../../core/toast/toast-service';

@Component({
  selector: 'app-report',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './report.html',
})
export class Report implements OnInit {

  constructor(private reportService: ReportService, private toastService: ToastService) { }

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

  ngOnInit() {
    this.reportForm.get('reason')?.valueChanges.subscribe(value => {
      const details = this.reportForm.get('details');
      if (value === 'other') {
        details?.setValidators(Validators.required);
      } else {
        details?.clearValidators();
      }
      details?.updateValueAndValidity();
    });
  }


  onSubmit() {
    const report: ReportInput = {
      postId: this.postId,
      reporterId: this.reporterId,
      reason: this.reportForm.value.reason! == "other" ? this.reportForm.value.details! : this.reportForm.value.reason!
    }
    if (report.reason == '') {
      this.toastService.warning(4000, "warning", "failed to create a report")
      return;
    };
    this.reportService.postReport(report).subscribe({
      next: _ => {
        this.toastService.success(4000, "report", "report was created successfully");
      },
      error: _ => {
        this.toastService.error(4000, "report", "failed to create a report");
      }
    })
  }
}
