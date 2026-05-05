import { Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from '../../../environment/environment'
import { LoginPayload, User, UserResponse, RegistrationFormData } from '../../../types/user';




@Injectable({
  providedIn: 'root',
})

export class Authentication {

  private readonly apiUrl = `${environment.apiUrl}/api/users`

  // create a user state subject to handle the state of the user so whenever the user changes it changes among all subscribers:
  public currentUser = new BehaviorSubject<UserResponse | null>(null);
  // observe the current subject to react properly to change:
  // hhh the dolar sign is just a convention to specify the observable
  public currentUser$ = this.currentUser.asObservable();
  public user = signal<UserResponse | null>(null);
  public isAdmin = computed(() => this.user()?.user?.isAdmin)


  constructor(private http: HttpClient) {
    // no manual assignment is needed: Angular's dependency injection handles this automatically.
    if(this.user() != null){
      this.loadStoredUser()
      this.checkStatus().subscribe()
    }
  }

  /**
   * Loads user data from localStorage if available.
   * Called on service initialization to restore session.
   */
  private loadStoredUser(): void {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      try {
        this.currentUser.next(JSON.parse(storedUser));
        this.user.set(JSON.parse(storedUser));
      } catch (error) {
        localStorage.removeItem('currentUser')
      }
    }
  }


  /**
   * Registers a new user.
   * @param user - The registration payload (User model).
   * @returns Observable<UserResponse> containing user and token.
   * On success:
   * - Stores token in localStorage.
   * - Updates currentUser$ observable.
   * - Logs registration event.
   */

  // register the user:
  register(user: RegistrationFormData): Observable<UserResponse> {
    const formData = new FormData();
    formData.append("email", user.email);
    formData.append("password", user.password);
    formData.append("firstName", user.firstName);
    formData.append("lastName", user.lastName);
    formData.append("nickname", user.nickname);
    formData.append("dateOfBirth", user.dateOfBirth);

    if (user.avatar) {
      formData.append("avatar", user.avatar);
    }
    return this.http.post<UserResponse>(`${this.apiUrl}/register`, formData)
  }

  /**
   * Logs in a user.
   * @param payload - Email and password for authentication.
   * @returns Observable<UserResponse> containing user and token.
   * On success:
   * - Stores token in localStorage
   * - Updates currentUser$ observable
   * - Logs login event
   */

  login(payload: LoginPayload): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${this.apiUrl}/login`, payload)
  }

  /**
 * Logs out the current user.
 * Clears token and user data from localStorage and memory.
 * 
 * Note: Makes API call to server for any server-side cleanup,
 * but doesn't wait for response since JWT is stateless.
 */
  logout(): Observable<void> {
    // Call backend logout endpoint (for any server-side cleanup)
    return this.http.delete<void>(`${this.apiUrl}/logout`).pipe(
      tap(() => this.clearSession()),
      catchError(error => {
        // Clear session even if API call fails
        this.clearSession();
        return throwError(() => error);
      })
    );
  }


  /**
 * Checks the current authentication status with the server.
 * Validates the stored token is still valid.
 * 
 * @returns Observable<UserResponse> with current user data if valid
 * 
 * Note: Token is automatically added by AuthInterceptor,
 * no need to manually add Authorization header here.
 */

  checkStatus(): Observable<UserResponse> {
    return this.http.get<UserResponse>(this.apiUrl).pipe(
      tap(response => {
        // Update user data if it changed on server
        this.currentUser.next(response);
        this.user.set(response);
        localStorage.setItem('user', JSON.stringify(response));
      }),
      catchError(error => {
        // If token is invalid, clear session
        if (error.status === 401) {
          this.clearSession();
        }
        return throwError(() => error);
      })
    );
  }

  /**
 * Clears all session data from localStorage and resets observable.
 */
  clearSession(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.currentUser.next(null);
    this.user.set(null)
  }


  /**
 * Stores user session data in localStorage and updates observable.
 * @param response - User response containing token and user data
 */
  setSession(response: UserResponse): void {
    localStorage.setItem('token', response.token);
    localStorage.setItem('currentUser', JSON.stringify(response));
    this.currentUser.next(response);
    this.user.set(response)
  }


}
