import { Component, Input, OnInit, signal, output } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PostService } from '../../../core/post/post-service';
import { Post, PostInput } from '../../../types/post';
import { finalize } from 'rxjs/operators';
import { ToastService } from '../../../core/toast/toast-service';
import { VALIDATION } from '../../../environment/validation-constants';

// ---------------------------------------------------------------------------
// Allowed MIME types per media category
// ---------------------------------------------------------------------------
const ALLOWED_IMAGE_TYPES = ['image/jpeg', 'image/png', 'image/gif', 'image/webp', 'image/svg+xml'];
const ALLOWED_VIDEO_TYPES = ['video/mp4', 'video/webm', 'video/ogg', 'video/quicktime', 'video/x-msvideo'];
 

@Component({
  selector: 'app-post-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './post-form.html',
})


export class PostForm implements OnInit {


  @Input() postToUpdate: Post | null = null;
  postSaved = output<Post>();

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

  constructor(private postService: PostService, private toastService: ToastService) {
    this.form = new FormGroup({
      title: new FormControl<string>('', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(VALIDATION.postTitle.min),
          Validators.maxLength(VALIDATION.postTitle.max),
        ],
      }),

      content: new FormControl<string>('', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(VALIDATION.postContent.min),
          Validators.maxLength(VALIDATION.postContent.max),
        ],
      }),

      mediaType: new FormControl<'image' | 'video' | null>(null),
      media: new FormControl<File | null>(null),

    },
     
    { validators: this.mediaFileValidator }

  );
  }

  ngOnInit(): void {
    if (this.postToUpdate) {
      this.updateState.set(true);
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

      // Trigger validation for mediaType when a file is selected
    this.form.controls.mediaType.updateValueAndValidity();
  }

  onSubmit() {
    if (this.form.invalid) return;

    this.isSubmitting = true;

    const postData: PostInput = {
      title: this.form.controls.title.value!,
      content: this.form.controls.content.value!,
      mediaType: this.form.controls.mediaType.value || undefined,
      media: this.form.controls.media.value || undefined
    };

    const request$ = this.updateState()
      ? this.postService.updatePost(this.post()?.id!, postData)
      : this.postService.createPost(postData);

    request$.pipe(
      finalize(() => this.isSubmitting = false)
    ).subscribe({
      next: (post) => {
        console.log("The new create post should be added immediately to the posts: ", post);
        this.postService.imitatePostSource(post);
        this.toastService.success(4000, "Post", "Post was created successfully");
        this.form.reset();
        this.fileName = '';
        this.postSaved.emit(post);
        if (this.updateState()) {
          this.updateState.set(false);
          this.post.set(null);
        }
      },
      error: (_) => this.toastService.error(4000, "Post", 'Failed to save post')
    });
  }

  // Cross-field validator: checks that the uploaded file matches the mediaType
// ---------------------------------------------------------------------------
 mediaFileValidator(group: AbstractControl): ValidationErrors | null {
  const mediaType = group.get('mediaType')?.value as 'image' | 'video' | null | '';
  const file = group.get('media')?.value as File | null;
 
  // No file selected – nothing to validate
  if (!file) return null;
 
  // No mediaType selected but a file was provided
  if (!mediaType) {
    return { mediaTypeMissing: true };
  }
 
  const mimeType = file.type;
 
  if (mediaType === 'image' && !ALLOWED_IMAGE_TYPES.includes(mimeType)) {
    return { invalidImageFile: true };
  }
 
  if (mediaType === 'video' && !ALLOWED_VIDEO_TYPES.includes(mimeType)) {
    return { invalidVideoFile: true };
  }
 
  return null;
}

}
