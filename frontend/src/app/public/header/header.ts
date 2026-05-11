import { Component, signal, computed, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Authentication } from '../../core/authentication/auth/authentication';
import { Router } from '@angular/router';
import { Store } from '../../core/store/store';
import { ReportService } from '../../core/report/report-service';
import { ToastService } from '../../core/toast/toast-service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.html',
})
export class Header implements OnInit {
  // Keep auth private:
  constructor(private auth: Authentication, private reportService: ReportService, private store: Store, private router: Router, private toastService: ToastService) { }

  reportsNumber = signal(0);

  isAdmin = computed(() => this.auth.user()?.user?.isAdmin);
  isLoggedIn = computed(() => this.auth.user() != null);
  user = computed(() => this.auth.user());

  num = 0;

  ngOnInit(): void {
    if(this.isAdmin()){
      this.updateReportsCount();
    }
  }

updateReportsCount(): void {
  this.reportService.getReportsCount().subscribe({
    next: (data) => {
      this.reportsNumber.set(data['count']);
    },
    error: (err) => {
      this.toastService.info(3000, "reports", err);
    }
  });
}
  

  // Wrapper method for template
  logout() {
    this.auth.logout().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Logout failed', err);
        this.router.navigate(['/login']);
      }
    });
  }


  login() {
    this.router.navigate(['/login']);
  }

  register() {
    this.router.navigate(['/register']);
  }

  goHome() {
    if(this.isAdmin()){ this.updateReportsCount()};
    this.router.navigate(['/']);
  }

goToProfile() {
  this.router.navigate(['/profile', this.user()?.user?.id]);
}

  goToUsersOrReports(){
     if(this.isAdmin()){ this.updateReportsCount()};
    const path = this.isAdmin() ? "/reports" : "/users";
    this.router.navigate([path]);
  }

  goToFeed(){
    this.num++;
    console.log("triggered goToFeed", this.num);
    
    if(this.isAdmin()){ this.updateReportsCount()};
    this.router.navigate(['/feed']);
  }
}
