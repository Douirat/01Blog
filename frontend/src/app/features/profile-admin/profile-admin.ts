import { Component, OnInit, signal } from '@angular/core';
import { UserDTO } from '../../types/user';
import { ActivatedRoute } from '@angular/router';
import { Authentication } from '../../core/authentication/auth/authentication';
import { UsersService } from '../../core/users/users-service';

@Component({
  selector: 'app-profile-admin',
  imports: [],
  templateUrl: './profile-admin.html',
  styleUrl: './profile-admin.scss',
})
export class ProfileAdmin implements OnInit {

user = signal<UserDTO | undefined>(undefined);
userId = signal<number | null>(null);

constructor(
  private route: ActivatedRoute,
  private auth: Authentication,
  private userService: UsersService
){}

ngOnInit() {
  const id = this.route.snapshot.params['id'];
   this.userService.getUserById(id).subscribe({
          next: userData => {
            console.log("the admin wants to visit the profile of: ", userData);
            
            // this.user.set(userData);
          },
          error: err => {
            console.error('Failed to load user', err);
          },
          complete: () => {
            console.log('User fetch completed');
          }
        });
}
}
