import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { map, catchError, tap } from 'rxjs/operators';
import { Authentication } from '../auth/authentication';
import { of } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(Authentication);
  const router = inject(Router);

  console.log('Auth guard triggered for:', state.url);

  return auth.checkStatus().pipe(
    map(res => {
      console.log('User status ---------------->', res);
      if (res?.user) {
        return true;
      } else {
        console.log('No user found, redirecting to login');
        auth.clearSession();
        router.navigate(['/login'], { 
          queryParams: { returnUrl: state.url } 
        });
        return false;
      }
    }),
    catchError(error => {
      console.error('Auth guard error:', error);
      auth.clearSession();
      router.navigate(['/login'], { 
        queryParams: { returnUrl: state.url } 
      });
      return of(false);
    })
  );
};