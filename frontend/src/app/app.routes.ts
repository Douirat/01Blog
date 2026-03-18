import { Routes } from '@angular/router';
// import { AdminComponent } from './features/admin/admin.component'; TODO: when you want to add the admin component it should be guarded using the role guard!
import { roleGuard } from './core/authentication/guards/role-guard'; // import the admin panel guarder.
import { Registration } from './features/authentication/registration/registration';
import { Login } from './features/authentication/login/login';
import { Dashboard } from './features/dashboard/dashboard';
import { authGuard } from './core/authentication/guards/auth-guard';
import { Users } from './features/users/users';
import { Profile } from './features/profile/profile';
import { AdminDashboard } from './features/admin-dashboard/admin-dashboard';
import { ProfileAdmin } from './features/profile-admin/profile-admin';

export const routes: Routes = [
      { path: 'admin', component: AdminDashboard, canActivate: [roleGuard] }, // the admin panel with its gate keeper.


    { path: 'login', component: Login },
    { path: 'register', component: Registration },

    {
        path: 'dashboard',
        component: Dashboard,
        canActivate: [authGuard]
    },
    {
        path: 'profile/:id',
        component: Profile,
        canActivate: []  // TODO: create another auth-guard to protect paths from non authenticated access.
    },
    {
        path: 'profile-admin/:id',
        component: ProfileAdmin,
        canActivate: []  // TODO: create another auth-guard to protect paths from non authenticated access.
    },
    {
        path: 'users',
        component: Users,
        canActivate: [authGuard]
    },
    // Fallback route (optional)
    { path: '**', redirectTo: '/dashboard' },

];
