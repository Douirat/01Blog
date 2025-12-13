import { Component, Input, OnInit, signal } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PostService } from '../../../core/post/post-service';
import { Post, PostInput } from '../../../types/post';



@Component({
  selector: 'app-post-form',
  standalone: true,
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

export class PostForm implements OnInit {

@Input() postToUpdate: Post | null = null;
  
  form: FormGroup<{
    title: FormControl<string>;
    content: FormControl<string>;
    mediaType: FormControl<'image' | 'video' | null>;
    media: FormControl<File | null>;
  }>;
  isSubmitting = false;
  fileName = ''
  updateState = signal(false)
  post = signal<Post | null>(null)

  constructor(private postService: PostService) {
    this.form = new FormGroup({
      title: new FormControl<string>('', { nonNullable: true, validators: Validators.required }),
      content: new FormControl<string>('', { nonNullable: true, validators: Validators.required }),
      mediaType: new FormControl<'image' | 'video' | null>(null),
      media: new FormControl<File | null>(null),
    });
  }

ngOnInit(): void {
  if (this.postToUpdate) {
    this.updateState.set(true);
    console.log("POST TO UPDATE: ", this.postToUpdate);
    this.post.set(this.postToUpdate);

    // Patch the form so the input fields show the existing values
    this.form.patchValue({
      title: this.postToUpdate.title,
      content: this.postToUpdate.content,
      mediaType: this.postToUpdate.mediaType || null,
      media: null, // file input cannot be pre-filled
    });

    // Show current media file name if exists
    if (this.postToUpdate.mediaUrl) {
      this.fileName = this.postToUpdate.mediaUrl.split('/').pop() || '';
    }
  }
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
