import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UserDTO } from '../../types/user';
import { PostService } from '../../core/post/post-service';
import { PaginatedPosts, Post } from '../../types/post';
import { PostForm } from '../dashboard/post-form/post-form';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, PostForm],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile implements OnInit {

  user = signal<UserDTO | null>(null);

  postToUpdate = signal<number | null>(null);

  // paginated related input:
  posts = signal<Post[]>([]);
  lastPage = signal(false);
  currentPage = signal(0);
  totalPages = signal(0);

  constructor(private postService: PostService, private router: Router) { }
  ngOnInit(): void {
    const state = window.history.state;
    this.user.set(state?.user ?? null);
    console.log("--------->User data:", this.user());

    if (!this.user()) {
      console.warn('No user passed!');
    }
    this.loadPosts();
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
    console.log("posts ---------> ", postId);
    this.postToUpdate.set(postId);
  }
}
