import { Component, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Authentication } from '../../core/authentication/auth/authentication';
import { UserDTO, PaginatedUsers } from '../../types/user';
import { UsersService } from '../../core/users/users-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-users',
  imports: [CommonModule],
  templateUrl: './users.html',
  styleUrl: './users.scss',
})
export class Users {

  constructor(private auth: Authentication, private usersService: UsersService, private router: Router) { }
  page = signal(0);
  users = signal<UserDTO[] | []>([])
  lastUsers = signal(false);
  totalUsers = signal(0);
  loggedUserId = signal<string | undefined>(undefined);

  // Search reliability
  searchTerm = signal("");
  searchPage = signal(0);

  ngOnInit(): void {
    const currentUser = this.auth.user();
    this.loggedUserId.set(currentUser?.user?.id);
    this.loadUsers()
  }

  // get all users:
  loadUsers(): void {
    this.usersService.fetchUsers(this.page()).subscribe((data: PaginatedUsers) => {

      this.users.set(data.content);
      // Assuming this.users is a WritableSignal<UserDTO[]>
      this.users.set(
        this.users().filter(user => user.id !== this.loggedUserId())
      );
      this.lastUsers.set(data.last);
      this.totalUsers.set(data.totalPages);
    })
  }

  visiteProfile(user: UserDTO): void {
    this.router.navigate(['/profile', user.id]);
  }

  // TODO: the navigation between pages is not fixed yet:

  /**
   * search for users
   */
  searchUsers() {
    const term = this.searchTerm().trim();

    if (!term) {
      // empty search → go back to normal feed
      this.loadUsers();
      return;
    }

    this.usersService.searchUsers(this.page(), term).subscribe((data: PaginatedUsers) => {
      this.users.set(
        data.content.filter(user => user.id !== this.loggedUserId())
      );
      this.lastUsers.set(data.last);
      this.totalUsers.set(data.totalPages);
    });
  }

  nextPage() {
  if (this.page() < this.totalUsers() - 1) {
    this.page.update(p => p + 1);
    this.loadUsers();
  }
}

previousPage() {
  if (this.page() > 0) {
    this.page.update(p => p - 1);
    this.loadUsers();
  }
}
}
