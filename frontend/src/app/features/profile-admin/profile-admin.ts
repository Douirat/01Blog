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


// TODO: i will have to bring all the reports for this specific profile.
constructor(
  private route: ActivatedRoute,
  private auth: Authentication,
  private userService: UsersService
){}


ngOnInit() {
  const id = this.route.snapshot.params['id'];
   this.userService.getUserById(id).subscribe({
          next: userData => {
            this.user.set(userData);
            console.log("the is is the user the admin wants to investigate: ", this.user());
          },
          error: err => {
            console.error('Failed to load user', err);
          },
          complete: () => {
            console.log('User fetch completed');
          },
        });
}



// Load reported posts:
loadUserReportedPosts():void{

}

// Load all user's posts:
loadUserPosts():void{

}

banUser(userId: number | undefined):void{
  console.log("the admin wants to ban this user: ", userId);
}
}
