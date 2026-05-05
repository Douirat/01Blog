import {Component, signal, OnInit } from '@angular/core';
import { Report } from '../../types/report';
import { ReportService } from '../../core/report/report-service';
import { CommonModule } from '@angular/common';
import { PostService } from '../../core/post/post-service';
import { Router } from '@angular/router';
import { ToastService } from '../../core/toast/toast-service';

@Component({
  selector: 'app-reports',
  imports: [CommonModule],
  templateUrl: './reports.html',
})
export class Reports implements OnInit{

reports = signal<Report[]>([]);
page = signal(0);
isLast = signal(false);
total = signal(0);
totalReports = signal(0);


public constructor(private reportService: ReportService, private postService: PostService, private router: Router, private toastService: ToastService){}

ngOnInit(): void {
  this.loadAllReports()
}

loadAllReports():void{
this.reportService.getAllReports(this.page()).subscribe({
          next: res => {
            if(res != null){
              this.reports.set(res.content);
              this.isLast.set(res.last);
              this.total.set(res.totalPages);
              this.totalReports.set(res.totalElements);
            }else{
              this.toastService.info(4000, "info", "No reports were declares.")
            }
          },
          error: _ => {
            this.toastService.info(4000, "info", "No reports were declares.")
          },
          complete: () => {
            this.toastService.info(3000, "Information", "Report fetch completed");
          }
        });
}

  nextPage() {
  if (this.page() < this.totalReports() - 1) {
    this.page.update(p => p + 1);
    this.loadAllReports();
  }
}

previousPage() {
  if (this.page() > 0) {
    this.page.update(p => p - 1);
    this.loadAllReports();
  }
}

gotoTheReported(postId: number){
    this.postService.getUserByPostId(postId).subscribe({
      next: u => {
         this.router.navigate(['/profile-admin', u.id]); 
      },
      error: _ =>{
        this.toastService.warning(3000, "warning", "Not found")
      },
    }
    )
    
}
}
