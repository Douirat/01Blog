import { HttpInterceptorFn} from '@angular/common/http';
import {Injectable } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

/**
 * HTTP Interceptor that automatically adds JWT token to requests.
 * 
 * This interceptor:
 * - Intercepts all outgoing HTTP requests
 * - Checks if user has a valid JWT token
 * - Adds Authorization header with Bearer token if present
 * - Forwards the modified request
 * 
 * Registration required in app.module.ts:
 * providers: [
 *   { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
 * ]
 */
Injectable()
export const httpInterceptor: HttpInterceptorFn = (req, next) => {
  // const authService = inject(Authentication);
  const token = localStorage.getItem('token');

  // skip attaching token for login/register endpoints:
  const isAuthReq = req.url.includes('/login') || req.url.includes('register')
  const authReq = token && !isAuthReq
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;
  // TODO: Errors will be handled globally here like 404 and 403:
   return next(authReq).pipe(
    catchError(error => {
      // Global error handling
      if (error.status === 401) {
        // Handle unauthorized
        localStorage.removeItem('token');
        // Redirect to login
      }
      if (error.status === 403) {
        // Handle forbidden
      }
      return throwError(() => error);
    })
  );
};
