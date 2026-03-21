import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environment/environment';
import { Report, ReportInput } from '../../types/report';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
   private readonly reportApiUrl = `${environment.apiUrl}/api/reports`
  constructor(private http: HttpClient){}
  // create a report:
  postReport(report: ReportInput):Observable<Report>{
    return this.http.post<Report>(this.reportApiUrl, report)
  }

  // get reports count for notification:
  getReportsCount():Observable<Record<string, number>>{
    return this.http.get<Record<string, number>>(this.reportApiUrl+"/count");
  }
}

