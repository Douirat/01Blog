import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from '../../../environment/environment'
import { LoginPayload, User, UserResponse } from '../../../types/user';


export interface AuthenticationService {
  register(user: User): Observable<UserResponse>
  login(payload: LoginPayload): Observable<UserResponse>
}

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

  constructor(private http: HttpClient) {
    // no manual assignment is needed: Angular's dependency injection handles this automatically.
    this.loadStoredUser();
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
      } catch (error) {
        console.error('[AuthService] Failed to parse stored user', error);
        localStorage.removeItem('currentUser')
      }
    }

  }


  /**
   * Registers a new user.
   * @param user - The registration payload (User model).
   * @returns Observable<UserResponse> containing user and token.
   * 
   * On success:
   * - Stores token in localStorage.
   * - Updates currentUser$ observable.
   * - Logs registration event.
   */

  // register the user:
  register(user: User): Observable<UserResponse> {
    console.log('[AuthService] Registering user: ', user);
    return this.http.post<UserResponse>(`${this.apiUrl}/register`, user).pipe(
      tap(response => {
        // store the token and user data:
        this.setSession(response)
        console.log('[AuthService] Registration successful');
      }),
      catchError(this.handleError('register'))
    )
  }

  /**
   * Logs in a user.
   * @param payload - Email and password for authentication.
   * @returns Observable<UserResponse> containing user and token.
   * 
   * On success:
   * - Stores token in localStorage
   * - Updates currentUser$ observable
   * - Logs login event
   */

  login(payload: LoginPayload): Observable<UserResponse> {
    console.log('[AuthService]: loggin in user', payload);
    return this.http.post<UserResponse>(`${this.apiUrl}/login`, payload).pipe(
      tap(response => {
        this.setSession(response)
        console.log('[AuthService] Login successful')
      }),
      catchError(this.handleError('login'))
    )
  }

  /**
 * Logs out the current user.
 * Clears token and user data from localStorage and memory.
 * 
 * Note: Makes API call to server for any server-side cleanup,
 * but doesn't wait for response since JWT is stateless.
 */
  logout(): Observable<void> {
    console.log('[AuthService] Logging out user');

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
  }


  /**
 * Stores user session data in localStorage and updates observable.
 * @param response - User response containing token and user data
 */
  private setSession(response: UserResponse): void {
    localStorage.setItem('token', response.token);
    localStorage.setItem('currentUser', JSON.stringify(response));
    this.currentUser.next(response);
  }

  /**
 * Centralized error handler for authentication operations.
 * Provides consistent error logging and user-friendly error messages.
 * 
 * @param operation - Name of the operation that failed
 * @returns Error handler function
 */
  private handleError(operation: string) {
    return (error: HttpErrorResponse): Observable<never> => {
      console.error(`[AuthService] ${operation} failed:`, error);

      // Create user-friendly error message based on status code
      let errorMessage = 'An unexpected error occurred';

      if (error.error instanceof ErrorEvent) {
        // Client-side error
        errorMessage = `Network error: ${error.error.message}`;
      } else {
        // Server-side error
        switch (error.status) {
          case 400:
            errorMessage = error.error?.message || 'Invalid request data';
            break;
          case 401:
            errorMessage = 'Invalid credentials';
            break;
          case 409:
            errorMessage = 'User already exists';
            break;
          case 500:
            errorMessage = 'Server error. Please try again later';
            break;
          default:
            errorMessage = error.error?.message || errorMessage;
        }
      }

      return throwError(() => new Error(errorMessage));
    };
  }
}
