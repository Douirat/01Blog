import { Component, OnInit, OnDestroy, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostForm } from './post-form/post-form';
import { UserResponse } from '../../types/user';
import { PaginatedPosts, Post } from '../../types/post'
import { Authentication } from '../../core/authentication/auth/authentication';
import { PostService } from '../../core/post/post-service'
import { Subscription } from 'rxjs';
import { CommentForm } from './comment-form/comment-form';
import { PostComments } from './post-comments/post-comments';
import { VoteRequest } from '../../types/vote';


@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, PostForm, CommentForm, PostComments],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit, OnDestroy {
  currentUser: UserResponse | null = null;
  private subscription?: Subscription;

  // pagination related input
  posts = signal<Post[]>([]);
  lastPage = signal(false);
  currentPage = signal(0);
  totalPages = signal(0);


  commentVisibility = signal<{ [postId: number]: boolean }>({});




  constructor(private authentication: Authentication,
    private postService: PostService
  ) { }


  ngOnInit(): void {
    this.subscription = this.authentication.currentUser$.subscribe(user => {
      this.currentUser = user
      console.log('Current user changed:', user);
    });
    this.loadPosts();
    console.log("The loaded posts: ", this.posts);
  }


  ngOnDestroy(): void {
    this.subscription?.unsubscribe(); // prevent memory leaks
  }


  loadPosts() {
    this.postService.fetchPosts(this.currentPage()).subscribe((data: PaginatedPosts) => {
      console.log("The loaded posts: ", data);
      this.posts.set(data.content);
      this.lastPage.set(data.last);
      this.totalPages.set(data.totalPages);
    });
  }

  // ✅ Toggle single post comment form:
  toggleCommentForm(postId: number) {
    this.commentVisibility.update(state => {
      const newState: { [id: number]: boolean } = {};

      // Mark everything as false
      for (const id in state) {
        newState[id] = false;
      }

      // Toggle only the clicked post
      newState[postId] = !state[postId];

      return newState;
    });
  }


  // ✅ Read from the signal:
  showCommentForm(postId: number) {
    return this.commentVisibility()[postId] === true;
  }

  // get all comments for a specific post.
  nextPage() {
    if (!this.lastPage()) {
      this.currentPage.update(p => p + 1);
      this.loadPosts();
    }
  }

  prevPage() {
    if (this.currentPage() > 0) {
      this.currentPage.update(p => p - 1);
      this.loadPosts();
    }
  }

  // handle the post's vote:
  vote(postId: number, value: 'like' | 'dislike') {
    let decision: boolean = false;
    if (value == 'like') {
      decision = true
    }
    let voteRequest: VoteRequest = {
      postId: postId,
      value: decision
    }
    this.postService.createVote(voteRequest).subscribe({
      next: (response) => {
        console.log('Vote response:', response.message);
        if (response.success) {
          // post.likes += 1; // or post.dislikes += 1 if value = false
          console.log("The vote response: ", response);
          
        }
      },
      error: (err) => {
        console.error('Vote failed', err);
      }
    });

  }
}
