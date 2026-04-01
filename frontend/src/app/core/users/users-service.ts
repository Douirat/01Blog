import { Injectable } from '@angular/core';
import { environment } from '../../environment/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaginatedUsers, UserDTO } from '../../types/user';

@Injectable({
  providedIn: 'root',
})

export class UsersService {
  private readonly apiUrl = `${environment.apiUrl}/api/profiles`

  constructor(private http: HttpClient) { }
  fetchUsers(page: number): Observable<PaginatedUsers> {
    const params = new HttpParams().set('page', page.toString());
    return this.http.get<PaginatedUsers>(this.apiUrl, { params });
  }

  // get a user by id:
  getUserById(userId: number | null): Observable<UserDTO> {
    if (userId == null) userId = 0;
    const params = new HttpParams().set('userId', userId.toString());
    return this.http.get<UserDTO>(this.apiUrl + '/user', { params });
  }

  banUser(userId: number): Observable<string> {
    let params = new HttpParams().set("id", userId);
    return this.http.patch<string>(this.apiUrl, null, { params })
  }

}
