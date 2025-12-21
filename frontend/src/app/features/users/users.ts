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
  totalUser = signal(0);
  loggedUserId = signal<string | undefined>(undefined);


  ngOnInit(): void {
    console.log("????");
    const currentUser = this.auth.user();
    this.loggedUserId.set(currentUser?.user?.id);
    this.loadUsers()
  }

  // get all users:
  loadUsers() {
    this.usersService.fetchUsers(this.page()).subscribe((data: PaginatedUsers) => {
     
      this.users.set(data.content);
      // Assuming this.users is a WritableSignal<UserDTO[]>
      this.users.set(
        this.users().filter(user => user.id !== this.loggedUserId())
      );

      this.lastUsers.set(data.last);
      this.totalUser.set(data.totalPages);
      console.log("the current user: ", this.loggedUserId());

    })
  }

  visiteProfile(user: UserDTO): void {
    console.log(`User ID clicked: ${user.id}`);
    this.router.navigate(['/profile', user.id]);
  }
}
