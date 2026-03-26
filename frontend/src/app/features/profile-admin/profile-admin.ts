import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserDTO } from '../../types/user';
import { ActivatedRoute } from '@angular/router';
// import { Authentication } from '../../core/authentication/auth/authentication';
import { UsersService } from '../../core/users/users-service';
import { PostService } from '../../core/post/post-service';
import { Post } from '../../types/post';

@Component({
  selector: 'app-profile-admin',
  imports: [CommonModule],
  templateUrl: './profile-admin.html',
  styleUrl: './profile-admin.scss',
})
export class ProfileAdmin implements OnInit {

  reportedPosts = signal<Post[] | []>([])
  user = signal<UserDTO | undefined>(undefined);
  userId = signal<number | undefined>(undefined);
  page = signal(0);
  isLast = signal(false);
  totalElements = signal(0);
  totalPages = signal(0);

  // TODO: i will have to bring all the reports for this specific profile.
  constructor(
    private route: ActivatedRoute,
    private userService: UsersService,
    private postService: PostService
  ) { }


  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    this.userId.set(id);
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
    this.loadReportedPosts();
  }



  // Load reported posts:
  loadReportedPosts(): void {
    this.postService.getReportedPosts(this.page(), this.userId()).subscribe({
      next: res => {
        if(res != null){
          this.reportedPosts.set(res.content);
          this.isLast.set(res.last);
          this.totalElements.set(res.totalElements);
          this.totalPages.set(res.totalPages);
        }
      },
      error: err => console.log(err),
    })
  }

  // Load all user's posts:
  loadUserPosts(): void {
  }

  banUser(userId: number | undefined): void {
    console.log("the admin wants to ban this user: ", userId);
  }
}
