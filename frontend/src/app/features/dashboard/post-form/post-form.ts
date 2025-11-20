import { Component } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PostService } from '../../../core/post/post-service';
import { Post, PostInput } from '../../../types/post';


@Component({
  selector: 'app-post-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './post-form.html',
  styleUrl: './post-form.scss',
})

/*
 export interface PostInput {
  title: string;
  content: string;
  mediaType?: 'image' | 'video';
  media?: File;
} 
  */

export class PostForm {
    form: FormGroup<{
    title: FormControl<string>;
    content: FormControl<string>;
    mediaType: FormControl<'image' | 'video' | null>;
    media: FormControl<File | null>;
  }>;
  isSubmitting = false;
  fileName = ''
  

  constructor(private postService: PostService) {
  this.form = new FormGroup({
      title: new FormControl<string>('', { nonNullable: true, validators: Validators.required }),
      content: new FormControl<string>('', { nonNullable: true, validators: Validators.required }),
      mediaType: new FormControl<'image' | 'video' | null>(null),
      media: new FormControl<File | null>(null),
    });
  }

    // handle file input change
  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;

    const file = input.files[0];
    this.form.controls.media.setValue(file);
    this.fileName = file.name;
  }

   onSubmit() {
    if (this.form.invalid) return;

    this.isSubmitting = true;

    const postData: PostInput = {
      title: this.form.controls.title.value,
      content: this.form.controls.content.value,
      mediaType: this.form.controls.mediaType.value || undefined,
      media: this.form.controls.media.value || undefined,
    };

    this.postService.createPost(postData).subscribe({
      next: (post: Post) => {

        this.form.reset();         // reset form
        this.fileName = '';
        this.isSubmitting = false;
      },
      error: (err: any) => {
        console.error('Failed to create post', err);
        this.isSubmitting = false;
      }
    });
  }

}
