import { Routes } from '@angular/router';
// import { AdminComponent } from './features/admin/admin.component'; TODO: when you want to add the admine component it should be guarded using the role guard!
// import { roleGuard } from './core/auth/role.guard'; // import the admin panel guarder.
import { Registeration } from './features/authentication/registeration/registeration';
import { Login } from './features/authentication/login/login';
import { Dashboard } from './features/dashboard/dashboard';
// import { authGuard } from './core/authentication/guards/auth-guard';

export const routes: Routes = [
    //   { path: 'admin', component: AdminComponent, canActivate: [roleGuard] }, // the admin panel with its gate keeper.
    // other routes...
    { path: 'login', component: Login },
    { path: 'register', component: Registeration },

    {
        path: 'dashboard',
        component: Dashboard,
        // canActivate: [authGuard]
    },
    {
        path: '',
        redirectTo: '/dashboard',
        pathMatch: 'full'
    },
    // Fallback route (optional)
    { path: '**', redirectTo: '/dashboard' }
    //   { 
    //     path: 'profile', 
    //     component: ProfileComponent,
    //     canActivate: [httpInterceptor]  // Protected route
    //   }
];
