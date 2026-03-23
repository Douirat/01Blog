import { Component, inject, Input, signal, OnChanges } from '@angular/core';
import { CommentService } from '../../../core/comment/comment-service';
import { CommonModule } from '@angular/common';
import { Comment } from '../../../types/comment';

@Component({
  standalone: true,
  selector: 'app-post-comments',
  imports: [CommonModule],
  templateUrl: './post-comments.html',
  styleUrl: './post-comments.scss',
})
export class PostComments {
  @Input() postId!: number;

  private commentService = inject(CommentService);

  comments = signal<Comment[]>([]);
  page = signal(0)
  totalPages = signal(0);
  lastPage = signal(false);



  ngOnChanges() {
    if (this.postId) {
      this.loadComments()
    }
  }

  //  Load comments:
  loadComments() {
    this.commentService.getComments(this.postId, this.page()).subscribe(res => {
      this.comments.set(res?.content);
      this.totalPages.set(res.totalPages);
      this.lastPage.set(res.last);
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
