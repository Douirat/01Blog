import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Report } from '../../features/dashboard/report/report';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
   private readonly reportApiUrl = `${environment.apiUrl}/api/report`
  constructor(private http: HttpClient){}
  post_report(report: Report):Observable<Report>{
    return this.http.post<Report>(this.reportApiUrl, report)
  }
}

