import { Component, inject, Input, signal } from '@angular/core';
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
 

 onChange(){
  if(this.postId){
    console.log("______-__-______", this.postId);
  }
 }
}
