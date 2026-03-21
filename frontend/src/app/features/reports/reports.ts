import {Component, signal, OnInit } from '@angular/core';
import { Report } from '../../types/report';
import { ReportService } from '../../core/report/report-service';
import { CommonModule } from '@angular/common';

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


public constructor(private reportService: ReportService){}

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
            console.log("the admin want to see all reports: ", this.reports(), this.totalReports(), this.isLast(), this.total());
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
    console.log("the reported post is: ", postId);
}
}
