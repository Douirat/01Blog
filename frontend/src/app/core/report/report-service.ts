import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environment/environment';
import { Report, ReportInput } from '../../types/report';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
   private readonly reportApiUrl = `${environment.apiUrl}/api/report`
  constructor(private http: HttpClient){}
  postReport(report: ReportInput):Observable<Report>{
    return this.http.post<Report>(this.reportApiUrl, report)
  }
}

