import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { map, catchError } from 'rxjs/operators';
import { Authentication } from '../auth/authentication';
import { of } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(Authentication);
  const router = inject(Router);

  console.log('Auth guard triggered for:', state.url);

  return auth.checkStatus().pipe(
    map(res => {
      const user = res?.user;
      

      //  User not logged in
      if (!user) {
        console.log('No user found, redirecting to login');
        auth.clearSession();
        router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
        return false;
      }

      // Admin user
      if (user.isAdmin) {
        if (state.url !== '/admin') {
          console.log('Admin user detected, redirecting to admin dashboard');
          router.navigate(['/admin']);
          return false; // prevent route activation, redirected
        }
        return true; // already on admin dashboard, allow
      }

      // Regular user
      if (state.url !== '/dashboard') {
        console.log('Regular user detected, redirecting to user dashboard');
        router.navigate(['/dashboard']);
        return false; // prevent route activation, redirected
      }

      return true; // already on regular dashboard, allow
    }),
    catchError(error => {
      console.error('Auth guard error:', error);
      auth.clearSession();
      router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
      return of(false);
    })
  );
};