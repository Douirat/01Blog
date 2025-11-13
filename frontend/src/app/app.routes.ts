import { Routes } from '@angular/router';
// import { AdminComponent } from './features/admin/admin.component'; TODO: when you want to add the admine component it should be guarded
// import { roleGuard } from './core/auth/role.guard'; // import the admin panel guarder.
// import { httpInterceptor } from './core/authentication/interceptors/http-interceptor';
import { Registeration } from './features/authentication/registeration/registeration';
import { Login } from './features/authentication/login/login';

export const routes: Routes = [
//   { path: 'admin', component: AdminComponent, canActivate: [roleGuard] }, // the admin panel with its gate keeper.
// other routes...
{ path: 'login', component: Login },
{ path: 'register', component: Registeration },

//   { 
//     path: 'dashboard', 
//     component: DashboardComponent,
//     canActivate: [httpInterceptor]  // Protected route
//   },
//   { 
//     path: 'profile', 
//     component: ProfileComponent,
//     canActivate: [httpInterceptor]  // Protected route
//   }
];
