import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { map, catchError } from 'rxjs/operators';
import { Authentication } from '../auth/authentication';
import { of } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(Authentication);
  const router = inject(Router);

  return auth.checkStatus().pipe(
    map(res => {
      if (res?.user){
        if(res?.user?.isAdmin){

             return router.createUrlTree(['/admin'], {
        queryParams: { returnUrl: state.url }
      });
        } else{
          return true;
        }
        
      }

      return router.createUrlTree(['/login'], {
        queryParams: { returnUrl: state.url }
      });
    }),
    catchError(() =>{
       console.log("this is what causes the issue !!!!");
      router.createUrlTree(['/login'], {
        queryParams: { returnUrl: state.url }
  
    });    return of(false);
    }
      
      
    )
  );
};