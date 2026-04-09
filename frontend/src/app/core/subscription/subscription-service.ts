import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environment/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SubscriptionService {
  private readonly APIUrl = `${environment.apiUrl}/api/subscription`
  constructor(private http: HttpClient) { }

  createSubscription(followedId: number): Observable<Record<string, string>> {
    let params = new HttpParams().set("followedId", followedId.toString())
    return this.http.post<Record<string, string>>(this.APIUrl, { params });
  }

  checkSubscription(followedId: number): Observable<Record<string, string>> {
    let params = new HttpParams().set("followedId", followedId.toString())
    return this.http.get<Record<string, string>>(this.APIUrl, { params });
  }
}
