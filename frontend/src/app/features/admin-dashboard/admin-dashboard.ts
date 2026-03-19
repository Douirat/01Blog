import { Component, OnInit, signal } from '@angular/core';
import { UserDTO, PaginatedUsers } from '../../types/user';
import { CommonModule } from '@angular/common';
import { UsersService } from '../../core/users/users-service';
import { Authentication } from '../../core/authentication/auth/authentication';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  imports: [CommonModule],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.scss',
})
export class AdminDashboard implements OnInit {

  users = signal<UserDTO[]>([]);
  page = signal(0);
  last = signal(false);
  loggedUserId = signal<string | undefined>(undefined);
  totalPages = signal(0);

  constructor(
    private auth: Authentication,
    private usersService: UsersService,
    private router: Router
  ) { }

  ngOnInit(): void {
    console.log("admin component has started. <><><><>");
    const currentUser = this.auth.user();
    this.loggedUserId.set(currentUser?.user?.id);
    this.loadUsers();
  }

  loadUsers(): void {
    this.usersService.fetchUsers(this.page()).subscribe((data: PaginatedUsers) => {
      this.users.set(data.content.filter(user => user.id !== this.loggedUserId()));
      this.last.set(data.last);
      this.totalPages.set(data.totalPages);

      console.log("Logged user ID:", this.loggedUserId());
    }
    );
  }

  //navigate between pages:
  nextPage(): void { if (!this.last) { this.page.update(p => p + 1); } this.loadUsers(); }

  prevPage(): void { if (this.page() > 0) { this.page.update(p => p - 1); } this.loadUsers(); }

  // Navigate to a user's profile
  adminVisitProfile(user: UserDTO): void { 
    console.log("uuuu", user);
    this.router.navigate(['/profile-admin', user.id]); 
  }
}
