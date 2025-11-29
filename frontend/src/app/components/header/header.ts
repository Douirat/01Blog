import { Component, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Authentication } from '../../core/authentication/auth/authentication';
import { Router } from '@angular/router';
import { UserResponse } from '../../types/user';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  // Keep auth private
  constructor(private auth: Authentication, private router: Router) {}

  isAdmin = computed(() => this.auth.user()?.user?.isAdmin);
  isLoggedIn = computed(() => this.auth.user() != null);
  user = computed(() => this.auth.user());

  // Wrapper method for template
  logout() {
    this.auth.logout();
    this.router.navigate(['/login']); // optional redirect
  }

  login() {
    console.log('Login clicked');
    this.router.navigate(['/login']); // optional redirect
  }
  register() {
    console.log('Register clicked');
    this.router.navigate(['/register']); // optional redirect
  }
}
