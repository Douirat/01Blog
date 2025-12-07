import { Injectable } from '@angular/core';
import { environment } from '../../environment/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaginatedUsersDTO } from '../../types/user';

@Injectable({
  providedIn: 'root',
})
export class UsersService {
  private readonly apiUrl = `${environment.apiUrl}/api/profiles`


  constructor(private http: HttpClient) { }

  fetchUsers(page: number):Observable<PaginatedUsersDTO>{
        const params = new HttpParams()
      .set('page', page.toString());
    return this.http.get<PaginatedUsersDTO>(this.apiUrl);
  }
  
}
