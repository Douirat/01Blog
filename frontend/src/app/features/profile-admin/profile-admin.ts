import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserDTO } from '../../types/user';
import { ActivatedRoute } from '@angular/router';
import { UsersService } from '../../core/users/users-service';
import { PostService } from '../../core/post/post-service';
import { Post } from '../../types/post';
import { ToastService } from '../../core/toast/toast-service';

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
    private postService: PostService,
    private toastService: ToastService
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
        console.log("res ", res);
        if (res != null) {
          this.reportedPosts.set(res.content);
          this.isLast.set(res.last);
          this.totalElements.set(res.totalElements);
          this.totalPages.set(res.totalPages);
        } else {
          this.toastService.info(3000, "Information", "No reported reported.")
        }
      },
      error: _ => this.toastService.info(3000, "error", "error loading repoted posts."),
    })
  }


  banUser(userId: number): void {
    this.userService.banUser(userId).subscribe({
      next: res => {
        this.toastService.success(3000, "success", "User banned successfully.");
      },
      error: err => {
        this.toastService.error(3000, "Error", "Error banning user.");
      },
    });
  }

  banPost(postId: number): void {
    this.postService.banPost(postId).subscribe({
      next: _ => {
        this.toastService.success(3000, "success", "Post banned successfully.");
      },
      error: _ => {
        this.toastService.error(3000, "Error", "Error banning post.");
      },
    });
  }

  activateUser(userId: number): void {
    this.userService.activateUserAccount(userId).subscribe({
      next: _ => {
        this.toastService.success(3000, "success", "user ban was deactivate.");
      },
      error: _ => {
        this.toastService.error(3000, "Error", "you cant activate this account.");
      }
    })
  }

  rejectReport(postId: number): void {
    console.log("the admin wants to reject ban of this post: ", postId);
  }
}
