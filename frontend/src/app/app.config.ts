import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { httpInterceptor } from './core/authentication/interceptors/http-interceptor';

import { routes } from './app.routes';

import { Authentication } from './core/authentication/auth/authentication';
import { CommentService } from './core/comment/comment-service';
import { PostService } from './core/post/post-service';
import { ReportService } from './core/report/report-service';
import { Store } from './core/store/store';
import { UsersService } from './core/users/users-service';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([httpInterceptor])),
    Authentication,
    CommentService,
    PostService,
    ReportService,
    Store,
    UsersService,
  ]
};
