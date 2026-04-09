import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environment/environment';
import { Observable } from 'rxjs';
import { SubscriptionResponse } from '../../types/subscription';

@Injectable({
  providedIn: 'root',
})
export class SubscriptionService {
  private readonly APIUrl = `${environment.apiUrl}/api/subscription`
  constructor(private http: HttpClient) { }

  handleSubscription(followedId: number): Observable<SubscriptionResponse> {
    let params = new HttpParams().set("followedId", followedId.toString())
    return this.http.post<SubscriptionResponse>(this.APIUrl, null, {params});
  }

  checkSubscription(followedId: number): Observable<SubscriptionResponse> {
    let params = new HttpParams().set("followedId", followedId.toString())
    return this.http.get<SubscriptionResponse>(this.APIUrl, {params});
  }
}
