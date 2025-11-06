import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environment/environment';


export interface AuthenticationService {

}

@Injectable({
  providedIn: 'root',
})
export class Authentication {
private readonly apiUrl = `${environment.apiUrl}`
}
