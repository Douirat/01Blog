import { CanActivateFn } from '@angular/router';
import { Authentication } from '../auth/authentication';
import { inject } from '@angular/core';
import { Router } from '@angular/router'
import {map} from 'rxjs/operators';

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(Authentication);
  const router = inject(Router);

  return auth.checkStatus().pipe(
    map(user => {
      if (user) return true;
      router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
      return false;
    })
  );
};
