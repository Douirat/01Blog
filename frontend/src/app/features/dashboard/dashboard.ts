import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostForm } from './post-form/post-form';
import { UserResponse } from '../../types/user';
import { PaginatedPosts, Post } from '../../types/post'
import { Authentication } from '../../core/authentication/auth/authentication';
import { PostService } from '../../core/post/post-service'
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, PostForm],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit, OnDestroy {
  currentUser: UserResponse | null = null;
  private subscription?: Subscription;
  posts: Post[] = [];
  lastPage = false;
  currentPage = 0;

  constructor(private authentication: Authentication, private postService: PostService) { }

  ngOnInit(): void {
    this.subscription = this.authentication.currentUser$.subscribe(user => {
      this.currentUser = user
      console.log('Current user changed:', user);
    });
    this.loadPosts();
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe(); // prevent memory leaks
  }

  loadPosts() {
    this.postService.fetchPosts(this.currentPage).subscribe((data: PaginatedPosts) => {
      console.log("The loaded posts: ", data);

      this.posts.push(...data.content);
      this.lastPage = data.last;
      if (!data.last) {
        this.currentPage++;
      }
    });
  }
}
