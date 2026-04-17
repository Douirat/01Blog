import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { UserDTO } from '../../types/user';
import { PostService } from '../../core/post/post-service';
import { PaginatedPosts, Post } from '../../types/post';
import { PostForm } from '../dashboard/post-form/post-form';
import { Authentication } from '../../core/authentication/auth/authentication';
import { UsersService } from "../../core/users/users-service";
import { ToastService } from '../../core/toast/toast-service';
import { SubscriptionService } from '../../core/subscription/subscription-service';



@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, PostForm],
  templateUrl: './profile.html',
})
export class Profile implements OnInit {

  user = signal<UserDTO | undefined>(undefined);
  userId = signal(0);
  profileOwner = signal<boolean>(false);
  loggedUser = signal<UserDTO | undefined>(undefined);

  postToUpdate = signal<number | null>(null);

  // paginated related input:
  posts = signal<Post[]>([]);
  lastPage = signal(false);
  currentPage = signal(0);
  totalPages = signal(0);

  // Subscription
  subscribed = signal(false);

  constructor(private route: ActivatedRoute,
    private postService: PostService,
    private auth: Authentication,
    private toastService: ToastService,
    private usersServ: UsersService,
    private subscriptionService: SubscriptionService
  ) { }

  ngOnInit(): void {
    this.loggedUser.set(this.auth.user()?.user);

    this.route.params.subscribe(params => {
      const id = params['id'];
      this.userId.set(id);
      if (this.loggedUser()?.id.toString() === id) {

        this.profileOwner.set(true);

        this.user.set(this.loggedUser());
      } else {
        this.usersServ.getUserById(this.userId()).subscribe({
          next: userData => {
            this.user.set(userData);
          },
          error: _ => {
            this.toastService.warning(3000, "warning", "Failed to load user")
          },
          complete: () => {
            console.log('User fetch completed');
          }
        });
      }
      this.loadPosts();
      if(this.user()?.id != this.loggedUser()?.id){
        this.checkFollowing();
      }
    });
  }

  // TODO: check subscription:
  checkFollowing(): void {
    this.route.params.subscribe(params => {
      const id = params['id'];
      this.subscriptionService.checkSubscription(id).subscribe({
        next: r => {
          this.subscribed.set(r.isFollowing);
        },
        error: _ => {
          this.toastService.warning(3000, "issue", "issue checking subscription");
        }
      });
    });
  }


  handleSubscription(): void {
    this.subscriptionService.handleSubscription(this.userId()).subscribe({
      next: res => {
        console.log(res);
        this.subscribed.set(res.isFollowing);
      },
      error: e => {
       this.toastService.warning(3000, "issue", "issue making subscription");
      }
    })
  }



  loadPosts() {
    let user = this.user()
    this.postService.getProfilePosts(this.currentPage(), Number(this.userId())).subscribe((data: PaginatedPosts) => {
      if (data) {
        this.posts.set(data.content);
        this.lastPage.set(data.last);
        this.totalPages.set(data.totalPages);
      }
    })
  }


  toggleUpdate(postId: number) {
    if (this.postToUpdate() === postId) {
      this.postToUpdate.set(null);
      return;
    }
    this.postToUpdate.set(postId);
  }


  onPostUpdated(updatedPost: Post) {
    this.posts.update(posts =>
      posts.map(p =>
        p.id === updatedPost.id ? updatedPost : p
      )
    );

    // close the form
    this.postToUpdate.set(null);
  }


  deletePost(postId: number): void {
    this.postService.deletePost(postId).subscribe({
      next: res => {
        this.toastService.success(4000, "success", "Post deleted successfully.");
        this.posts.set(this.posts().filter(p => p.id != postId));
      },
      error: err => {
        this.toastService.error(3000, "error", "Error deleting post.")
      },
    })
  }



  nextPage() {
    if (this.currentPage() < this.totalPages() - 1) {
      this.currentPage.update(p => p + 1);
      this.loadPosts();
    }
  }


  previousPage() {
    if (this.currentPage() > 0) {
      this.currentPage.update(p => p - 1);
      this.loadPosts();
    }
  }

  toStr(value: unknown): string {
    return String(value);
  }

}
