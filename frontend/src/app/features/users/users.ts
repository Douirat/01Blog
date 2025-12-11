import { Component, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Authentication } from '../../core/authentication/auth/authentication';
import { UserDTO, PaginatedUsers } from '../../types/user';
import { UsersService } from '../../core/users/users-service';

@Component({
  selector: 'app-users',
  imports: [CommonModule],
  templateUrl: './users.html',
  styleUrl: './users.scss',
})
export class Users {
  constructor(private auth: Authentication, private usersService: UsersService) { }
  page = signal(0);
  users = signal<UserDTO[] | []>([])
  lastUsers = signal(false);
  totalUser = signal(0);

  ngOnInit(): void {
    this.loadUsers()
  }

  // get all users:
  loadUsers() {
    this.usersService.fetchUsers(this.page()).subscribe((data: PaginatedUsers) => {
      this.users.set(data.content);
      this.lastUsers.set(data.last);
      this.totalUser.set(data.totalPages);
    })
  }

  visiteProfile(user: UserDTO): void {
    console.log(`User ID clicked: ${user.id}`);
    
    // Now you can use the ID to navigate or perform an action
    // Example: Navigate to the user's profile using a router service
    // this.router.navigate(['/profile', user.id]);
  }
}
