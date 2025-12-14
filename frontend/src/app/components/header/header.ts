import { Component, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Authentication } from '../../core/authentication/auth/authentication';
import { Router } from '@angular/router';
import { Store } from '../../core/store/store';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  // Keep auth private:
  constructor(private auth: Authentication, private store: Store, private router: Router) { }

  isAdmin = computed(() => this.auth.user()?.user?.isAdmin);
  isLoggedIn = computed(() => this.auth.user() != null);
  user = computed(() => this.auth.user());


  // Wrapper method for template
  logout() {
    this.auth.logout().subscribe({
      next: () => {
        console.log('User logged out');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Logout failed', err);
        this.router.navigate(['/login']);
      }
    });
  }


  login() {
    console.log('Login clicked');
    this.router.navigate(['/login']);
  }

  register() {
    console.log('Register clicked');
    this.router.navigate(['/register']);
  }

  goHome() {
    console.log('Home clicked');
    this.router.navigate(['/']);
  }

  goToProfile() {
    console.log('Profile clicked', this.user());
  this.router.navigate(['/profile'], { state: { profileOwner: true } });
  }

  seeUsers(){
    console.log('Pogin clicked');
    this.router.navigate(['/users']);
  }
}
