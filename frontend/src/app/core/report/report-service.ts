import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environment/environment';
import { PaginatedReports, Report, ReportInput } from '../../types/report';

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

  // get all reports:
  getAllReports(page: number):Observable<PaginatedReports>{
    let params = new HttpParams();
    params.set("page", page.toString());
    return this.http.get<PaginatedReports>(this.reportApiUrl, {params});
  }

  // get all reports for a specific user:
  getUserReports(userId:number | undefined = undefined, page: number ):Observable<PaginatedReports>{
    let params = new HttpParams();
    params.set("page", page.toString());
    if(userId != undefined){
      params.set("userId", userId?.toString());
    }
    return this.http.get<PaginatedReports>(this.reportApiUrl+"/user", {params});
  }

}

