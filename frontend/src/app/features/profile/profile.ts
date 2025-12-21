import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { UserDTO } from '../../types/user';
import { PostService } from '../../core/post/post-service';
import { PaginatedPosts, Post } from '../../types/post';
import { PostForm } from '../dashboard/post-form/post-form';
import { Authentication } from '../../core/authentication/auth/authentication';
import { UsersService } from "../../core/users/users-service";



@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, PostForm],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile implements OnInit {

  user = signal<UserDTO | undefined>(undefined);
  profileOwner = signal<boolean>(false);
  loggedUser = signal<UserDTO | undefined>(undefined);

  postToUpdate = signal<number | null>(null);

  // paginated related input:
  posts = signal<Post[]>([]);
  lastPage = signal(false);
  currentPage = signal(0);
  totalPages = signal(0);

  constructor(private route: ActivatedRoute, private postService: PostService, private auth: Authentication, private router: Router, private usersServ: UsersService) { }

  ngOnInit(): void {
    this.loggedUser.set(this.auth.user()?.user);

    this.route.params.subscribe(params => {
      const userId = params['id'];

      if (this.loggedUser()?.id === userId) {
        this.profileOwner.set(true);
        this.user.set(this.loggedUser());
      } else {
        this.usersServ.getUserById(userId).subscribe({
          next: userData => {
            this.user.set(userData);
          },
          error: err => {
            console.error('Failed to load user', err);
          },
          complete: () => {
            console.log('User fetch completed');
          }
        });
      }

      console.log('Navigated user:', userId);
    });
  }




  loadPosts() {
    let user = this.user()
    this.postService.fetchPosts(this.currentPage(), Number(user?.id)).subscribe((data: PaginatedPosts) => {
      if (data) {
        this.posts.set(data.content);
        this.lastPage.set(data.last);
        this.totalPages.set(data.totalPages);
      }
    })
  }

  toggleUpdate(postId: number) {
    this.postToUpdate.set(postId);
  }
}
