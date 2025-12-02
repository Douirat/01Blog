import { Component,  inject, Input, OnInit } from '@angular/core';
import { CommentService } from '../../../core/comment/comment-service';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { CommentResponse, CommentRequest } from '../../../types/comment';
import { CommonModule } from '@angular/common';


type CommentFormType = {
  title: FormControl<string>;
  content: FormControl<string>;
  postId: FormControl<number>;
};

@Component({
  selector: 'app-comment-form',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './comment-form.html',
  styleUrl: './comment-form.scss',
})
export class CommentForm implements OnInit {

  @Input() postId!: number;

  ngOnInit() {
    this.form.patchValue({ postId: this.postId });
  }

  isLoading = false;
  responseMessage: CommentResponse | null = null;
  newCommentId: number | null = null;
  isSuccess: boolean | null = null;
  service: CommentService = inject(CommentService);

  // Declare the form group as a wrapper of the request type:


  form: FormGroup<CommentFormType> = new FormGroup<CommentFormType>({
    title: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    content: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    postId: new FormControl(0, { nonNullable: true, validators: [Validators.required] }),
  });



  onSubmit() {
    if (this.form.valid) {
      this.isLoading = true;
      const commentRequest: CommentRequest = this.form.getRawValue();

      this.service.createComment(commentRequest).subscribe({
        next: (response) => {
          this.responseMessage = response;
          this.newCommentId = response.id; // if response has an id
          this.isSuccess = true;
          this.isLoading = false;
          this.form.reset(); // Optional: reset form after success
        },
        error: (error) => {
          this.isSuccess = false;
          this.isLoading = false;
          console.error('Error creating comment:', error);
        }
      });
    }
  }

}
