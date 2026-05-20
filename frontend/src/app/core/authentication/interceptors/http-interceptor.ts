import { HttpInterceptorFn} from '@angular/common/http';
import {Injectable, inject } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import {Router} from '@angular/router';
import { ToastService } from '../../toast/toast-service';
import { Authentication } from '../auth/authentication';

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
  const router = inject(Router);
  const toastService = inject(ToastService);
  const auth = inject(Authentication);

  // skip attaching token for login/register endpoints:
  const isAuthReq = req.url.includes('/login') || req.url.includes('register')
  const authReq = token && !isAuthReq
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;
    
   return next(authReq).pipe(
    catchError(error => {
      // Global error handling
      if (error.status === 401) {
        // Handle unauthorized
        localStorage.removeItem('token');
         localStorage.removeItem('user');
        // Redirect to login
        router.navigate(['/login']);
      }
      if (error.status === 403) {
        // Handle forbidden
       toastService.warning(4000, "Unauthorized", "You don't have permission to perform this action.");
       auth.clearSession();
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
