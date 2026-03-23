import {Component, signal, OnInit } from '@angular/core';
import { Report } from '../../types/report';
import { ReportService } from '../../core/report/report-service';
import { CommonModule } from '@angular/common';
import { PostService } from '../../core/post/post-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reports',
  imports: [CommonModule],
  templateUrl: './reports.html',
  styleUrl: './reports.scss',
})
export class Reports implements OnInit{

reports = signal<Report[]>([]);
page = signal(0);
isLast = signal(false);
total = signal(0);
totalReports = signal(0);


public constructor(private reportService: ReportService, private postService: PostService, private router: Router){}

ngOnInit(): void {
  this.loadAllReports()
}

loadAllReports():void{
this.reportService.getAllReports(this.page()).subscribe({
          next: res => {
        
            this.reports.set(res.content);
            this.isLast.set(res.last);
            this.total.set(res.totalPages);
            this.totalReports.set(res.totalElements);
          },
          error: err => {
            console.error('Failed to load reports', err);
          },
          complete: () => {
            console.log('User fetch completed');
          }
        });
}

loadNextPage(){

}

loadPrevPage(){

}

gotoTheReported(postId: number){
    this.postService.getUserByPostId(postId).subscribe({
      next: u => {
        
         this.router.navigate(['/profile-admin', u.id]); 
      },
      error: e =>{
        console.log("TODO: i will have to create the pop up mechanism for error visibility.");
        
      },
    }
    )
    
}
}
