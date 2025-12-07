import { Component, signal } from '@angular/core';
import { Authentication } from '../../core/authentication/auth/authentication';
import { UserDTO, PaginatedUsersDTO } from '../../types/user';
import { UsersService } from '../../core/users/users-service';

@Component({
  selector: 'app-users',
  imports: [],
  templateUrl: './users.html',
  styleUrl: './users.scss',
})
export class Users {
  constructor(private auth: Authentication, private usersService: UsersService) { }
  page:number = 0;
  users = signal<UserDTO[] | []>([])

  // get all users:
  get_all_users() {
    
  }
}
