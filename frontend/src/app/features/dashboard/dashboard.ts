import { Component, OnInit, OnDestroy, signal } from '@angular/core';
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
import {Report} from './report/report'


@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, PostForm, CommentForm, PostComments, Report],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit, OnDestroy {
  user = signal<UserResponse | null>(null);

  private subscription?: Subscription;

  // pagination related input:
  posts = signal<Post[]>([]);
  lastPage = signal(false);
  currentPage = signal(0);
  totalPages = signal(0);
  postToReport = signal(0)


  commentVisibility = signal<{ [postId: number]: boolean }>({});


  constructor(private authentication: Authentication,
    private postService: PostService
  ) { }


  ngOnInit(): void {
    this.user.set(this.authentication.user())
    console.log('Current user changed:', this.user());
    this.loadPosts();
    console.log("The loaded posts: ", this.posts());
  }


  ngOnDestroy(): void {
    this.subscription?.unsubscribe(); // prevent memory leaks.
  }


  loadPosts() {
    console.log("the loged user is: ", this.user());
    
    this.postService.fetchPosts(this.currentPage()).subscribe((data: PaginatedPosts) => {
      console.log("The loaded posts: ", data);
      if( data ){
        this.posts.set(data.content);
        this.lastPage.set(data.last);
        this.totalPages.set(data.totalPages);
      }
    });
  }

  //  Toggle single post comment form:
  toggleCommentForm(postId: number) {
    this.commentVisibility.update(state => {
      const newState: { [id: number]: boolean } = {};

      // Mark everything as false:
      for (const id in state) {
        newState[id] = false;
      }

      // Toggle only the clicked post:
      newState[postId] = !state[postId];

      return newState;
    });
  }


  // Read from the signal:
  showCommentForm(postId: number) {
    return this.commentVisibility()[postId] === true;
  }

  // get all comments for a specific post:
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
          this.posts

          this.posts.update(currentPosts =>
            currentPosts.map(post => {
              if (post.id === postId) {
                post.likes = response.postDTO.likes;
                post.dislikes = response.postDTO.dislikes;
                console.log("vote response: ----> ", response);
                console.log("post after update: ---> ", post);
              }
              return post;

            })
          )
        }
      },
      error: (err) => {
        console.error('Vote failed', err);
      }
    });
  }

  // create a method to report a post:
  reportPost(postId: number) {
    if (postId == this.postToReport()) {
      this.postToReport.set(0)
    } else {
      this.postToReport.set(postId)
    }
  }
}
