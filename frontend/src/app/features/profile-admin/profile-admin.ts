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


  isUserBanned = signal(false);

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
        if (this.user()?.banned) {
          this.isUserBanned.set(true)
        } else {
          this.isUserBanned.set(false)
        }
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
        this.isUserBanned.set(true)
      },
      error: err => {
        this.toastService.error(3000, "Error", "Error banning user.");
      },
    });
  }

  activateUser(userId: number): void {
    this.userService.activateUserAccount(userId).subscribe({
      next: _ => {
        this.toastService.success(3000, "success", "user ban was deactivated.");
        this.isUserBanned.set(false);
      },
      error: _ => {
        this.toastService.error(3000, "Error", "you cant activate this account.");
      }
    })
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

  unbanPost(postId: number): void{
    console.log("the admin wants to anban post with id:", postId);
  }


  rejectReport(postId: number): void {
     console.log("the admin wants to reject ost reports: =>", postId);
    this.postService.rejectReport(postId).subscribe({
      next: _ =>{
        this.toastService.success(4000, "success", "all reports were rejected successfully.")
        this.reportedPosts.set(this.reportedPosts().filter(p => p.id != postId));
      },
      error: _=>{
        this.toastService.error(3000, "error", "Error rejecting post reports.");
      },
    })
  }
}
