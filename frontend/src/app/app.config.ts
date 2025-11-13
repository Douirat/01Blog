import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { httpInterceptor } from './core/authentication/interceptors/http-interceptor';
import { Authentication } from './core/authentication/auth/authentication';

import { routes } from './app.routes';


export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([httpInterceptor])),
    Authentication, // Make sure to provide your service
  ]
};
