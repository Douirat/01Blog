import { Component, Input } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-post-comments',
  imports: [],
  templateUrl: './post-comments.html',
  styleUrl: './post-comments.scss',
})
export class PostComments {
 @Input() postId!: number;

}
