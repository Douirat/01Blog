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



@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, PostForm],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile implements OnInit {

  user = signal<UserDTO | undefined>(undefined);
  userId = signal<number | null>(null);
  profileOwner = signal<boolean>(false);
  loggedUser = signal<UserDTO | undefined>(undefined);

  postToUpdate = signal<number | null>(null);

  // paginated related input:
  posts = signal<Post[]>([]);
  lastPage = signal(false);
  currentPage = signal(0);
  totalPages = signal(0);

  constructor(private route: ActivatedRoute,
      private postService: PostService,
      private auth: Authentication,
      private toastService: ToastService,
      private usersServ: UsersService
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
    });
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

deletePost(postId: number): void{

}

}
