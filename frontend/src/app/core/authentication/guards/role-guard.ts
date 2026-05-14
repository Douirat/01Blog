import { CanActivateFn } from '@angular/router';
import { Authentication } from '../auth/authentication';
import { inject } from '@angular/core';
import { Router } from '@angular/router'
import { of } from 'rxjs';
import { map, catchError } from 'rxjs';

export const roleGuard: CanActivateFn = (route) => {
  const auth = inject(Authentication);
  const router = inject(Router);

  const requiredRole = route.data['role'];

  return auth.checkStatus().pipe(
    map(res => {
      const user = res?.user;

      if (!user) {
        return router.createUrlTree(['/login']);
      }

      if (requiredRole === 'ADMIN' && user.isAdmin) {
        return true;
      }

      return router.createUrlTree(['/forbidden']);
    }),
    catchError(() => {
      console.log("this is what causes the issue !!!!");
      return of(router.createUrlTree(['/login']));
    })
  );
};
  /** 
  * Where to use it:
 * - Attach this guard to any route that should be **accessible only by admin users**.
 * - Example usage in your routing module:
 *
 *   {
 *     path: 'admin',
 *     canActivate: [roleGuard], // protects the admin route
 *     loadComponent: () => import('./features/admin/admin.component').then(m => m.AdminComponent)
 *   }
 *
 * Effects on your program:
 * - Prevents unauthorized users from accessing sensitive routes like /admin.
 * - Keeps your UI secure by relying on backend validation rather than just localStorage.
 * - Centralizes role-based access logic, so you don’t have to scatter role checks in components.
 * - Works reactively: the guard waits for the backend response before allowing or denying navigation.
 */
