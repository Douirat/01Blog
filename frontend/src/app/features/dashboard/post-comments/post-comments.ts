import { Component, inject, OnInit, Input, OnChanges, signal } from '@angular/core';
import { CommentService } from '../../../core/comment/comment-service';
import { CommonModule } from '@angular/common';
import { Comment } from '../../../types/comment';
import { ToastService } from '../../../core/toast/toast-service';

@Component({
  standalone: true,
  selector: 'app-post-comments',
  imports: [CommonModule],
  templateUrl: './post-comments.html',
  styleUrl: './post-comments.scss',
})


export class PostComments implements OnChanges, OnInit {

  constructor(private toastservice: ToastService) { }

  @Input() postId!: number;

  private commentService = inject(CommentService);

  comments = signal<Comment[]>([]);
  page = signal(0)
  totalPages = signal(0);
  lastPage = signal(false);

  ngOnInit(): void {
    this.commentService.commentSource$.subscribe(comment => {
      this.comments.set([comment, ...this.comments()])
    })
  }


  ngOnChanges() {
    if (this.postId) {
      this.loadComments()
    }
  }

  //  Load comments:
  loadComments() {
    this.commentService.getComments(this.postId, this.page()).subscribe({
      next: res => {
        if (res != null) {
          this.comments.set(res?.content);
          this.totalPages.set(res.totalPages);
          this.lastPage.set(res.last);
        } else {
          this.toastservice.info(5000, "message", "seems like there are no comments");
        }
      },
      error: err => {
        this.toastservice.info(5000, "message", "seems like there are no comments");
      },
    })

  }


  nextPage() {
    if (!this.lastPage()) {
      this.page.update(p => p + 1);
      this.loadComments();
    }
  }

  prevPage() {
    if (this.page() > 0) {
      this.page.update(p => p - 1);
      this.loadComments();
    }
  }
}
