import { CanActivateFn } from '@angular/router';
import { Authentication } from '../auth/authentication';
import { inject } from '@angular/core';
import { Router } from '@angular/router'
import { of } from 'rxjs';
import { map, catchError } from 'rxjs';

export const roleGuard: CanActivateFn = (route, state) => {
  const auth = inject(Authentication)
  const router = inject(Router)
  // extract the user role from route metadata:
  const requiredRole = route.data['role'];

  // call the backend via auth service to validate token and get current user
  return auth.checkStatus().pipe(
    map(response => {
      // if backend returns user
      if (response && response.user) {
        const user = response.user
        // check role:
        // Only allow if the user is an admin
        if (user?.isAdmin) {
          return true;
        }
      }
      // if user doesn't have required role -> redirect
      router.navigate(['/forbidden']);
      return false;
    }),
    catchError(() => {
      // if the backent rejects the token -> redirect to login
      router.navigate(['/login'], { queryParams: { returnUrl: state.url } })
      return of(false)
    })
  );
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
};
