import { Component, signal, OnInit } from '@angular/core';
import { Report } from '../../types/report';

@Component({
  selector: 'app-reports',
  imports: [],
  templateUrl: './reports.html',
  styleUrl: './reports.scss',
})
export class Reports implements OnInit{
reports = signal<Report[]>([]);
ngOnInit(): void {
  
}

loadReports(){
  
}
}
