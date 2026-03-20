import { Component, signal, computed, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Authentication } from '../../core/authentication/auth/authentication';
import { Router } from '@angular/router';
import { Store } from '../../core/store/store';
import { ReportService } from '../../core/report/report-service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header implements OnInit {
  // Keep auth private:
  constructor(private auth: Authentication, private reportService: ReportService, private store: Store, private router: Router) { }

  reportsNumber = signal(0);

  isAdmin = computed(() => this.auth.user()?.user?.isAdmin);
  isLoggedIn = computed(() => this.auth.user() != null);
  user = computed(() => this.auth.user());

  ngOnInit(): void {
    this.updateReportsCount();
  }

updateReportsCount(): void {
  this.reportService.getReportsCount().subscribe({
    next: (data) => {
      this.reportsNumber.set(data['count']);
      console.log("the reports number is ----->", this.reportsNumber);
      
    },
    error: (err) => {
      console.error(err);
    }
  });
}
  

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
    if(this.isAdmin()){ this.updateReportsCount()};
    console.log('Home clicked');
    this.router.navigate(['/']);
  }

goToProfile() {
  console.log('Profile clicked', this.user());
  this.router.navigate(['/profile', this.user()?.user?.id]);
}

  goToUsersOrReports(){
     if(this.isAdmin()){ this.updateReportsCount()};
    const path = this.isAdmin() ? "/reports" : "/users";
    this.router.navigate([path]);
  }
}
