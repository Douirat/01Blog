import { Component, inject, Input, OnInit,OnChanges, signal } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { CommentRequest, Comment } from '../../../types/comment';
import { CommonModule } from '@angular/common';
import { SimpleChanges } from '@angular/core';
import { CommentService } from '../../../core/comment/comment-service';
import { VALIDATION } from '../../../environment/validation-constants';

type CommentFormType = {
  title: FormControl<string>;
  content: FormControl<string>;
  postId: FormControl<number | null>;
};

@Component({
  selector: 'app-comment-form',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './comment-form.html',
})


export class CommentForm implements OnChanges {

  isLoading = false;
  responseMessage: Comment | null = null;
  newCommentId: number | null = null;
  isSuccess: boolean | null = null;
  service: CommentService = inject(CommentService);

  constructor(private commentService: CommentService){}
  

  @Input() postId!: number;

  // Signal with initial undefined (correct)
  post_id = signal<number | null>(null);




  ngOnChanges(changes: SimpleChanges) {
    if (changes['postId'] && this.postId) {
      this.post_id.set(this.postId);   // update the signal
      this.form.patchValue({ postId: this.post_id() }); // update the form directly
    }
  }


  // Declare the form group as a wrapper of the request type:
  form: FormGroup<CommentFormType> = new FormGroup<CommentFormType>({
    title: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(VALIDATION.commentTitle.min), Validators.maxLength(VALIDATION.commentTitle.max)] }),
    content: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(VALIDATION.commentContent.min), Validators.maxLength(VALIDATION.commentContent.max)] }),
    postId: new FormControl(null as any, { validators: [Validators.required] }),
  });



  onSubmit() {
    if (this.form.valid) {
      this.isLoading = true;
      const commentRequest: CommentRequest = this.form.getRawValue();


      this.service.createComment(commentRequest).subscribe({
        next: (response) => {
          this.commentService.imitateComment(response);
          this.responseMessage = response;
          this.newCommentId = response.id; // if response has an id
          this.isSuccess = true;
          this.isLoading = false;
          this.form.reset({ postId: this.post_id() });
          setTimeout(() => {
            this.isSuccess = null
          }, 2000);
        },
        error: (error) => {
          // this.isSuccess = false;
          this.isLoading = false;
          console.error('Error creating comment:', error);
        }
      });
    } else {
      this.isSuccess = false;
    }
  }

}
