import { Routes } from '@angular/router';
// import { AdminComponent } from './features/admin/admin.component'; TODO: when you want to add the admine component it should be guarded using the role guard!
// import { roleGuard } from './core/auth/role.guard'; // import the admin panel guarder.
import { Registeration } from './features/authentication/registeration/registeration';
import { Login } from './features/authentication/login/login';
import { Dashboard } from './features/dashboard/dashboard';
import { authGuard } from './core/authentication/guards/auth-guard';
import { Users } from './features/users/users';
import { Profile } from './features/profile/profile';


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
        path: 'profile',
        component: Profile,
        canActivate: [authGuard]  // Protected route
    },
    {
        path: 'users',
        component: Users,
        canActivate: [authGuard]
    },
    // Fallback route (optional)
    { path: '**', redirectTo: '/dashboard' },

];
