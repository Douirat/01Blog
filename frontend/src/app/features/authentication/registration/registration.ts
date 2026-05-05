import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidatorFn, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { Authentication } from '../../../core/authentication/auth/authentication'; // adjust to your path
import { RegistrationFormData } from '../../../types/user'; // adjust to your path
import { ToastService } from '../../../core/toast/toast-service';
import { VALIDATION } from '../../../environment/validation-constants';


@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './registration.html',
})
export class Registration {
  form: FormGroup;
  isSubmitting = false; // Track the submission state.
  errorMessage = ''; // Store error message.

  selectedFile: File | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: Authentication, // Inject the authentication service.
    private router: Router, // Inject the router.
    private toastService: ToastService
  ) {
    // Initializing the form with validation rules:
    this.form = this.fb.group({
      email: ['', [
        Validators.required,
        Validators.email,
        Validators.minLength(VALIDATION.email.min),
        Validators.maxLength(VALIDATION.email.max),
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(VALIDATION.password.min),
        Validators.maxLength(VALIDATION.password.max),
      ]],
      firstName: ['', [
        Validators.required,
        Validators.minLength(VALIDATION.name.min),
        Validators.maxLength(VALIDATION.name.max),
      ]],
      lastName: ['', [
        Validators.required,
        Validators.minLength(VALIDATION.name.min),
        Validators.maxLength(VALIDATION.name.max),
      ]],
      dateOfBirth: ['', [Validators.required, this.ageRangeValidator(18, 120)]],
      avatar: [null],
      nickname: ['', [
        Validators.required,
        Validators.minLength(VALIDATION.nickname.min),
        Validators.maxLength(VALIDATION.nickname.max),
      ]],
    });
  }

  /**
   * Handles form submission
   * This method is called when the user clicks "create account"
   */
  onSubmit() {
    if (this.form.valid) {
      // set the loading state:
      this.isSubmitting = true;
      this.errorMessage = '';


      // get the form values and create the user object:
      const newUser: RegistrationFormData = this.form.value;

      if (this.selectedFile) {
        newUser.avatar = this.selectedFile; // IMPORTANT
      }

      // call the authentication service to register the user:
      this.authService.register(newUser).subscribe({
        // Success handler:
        next: (res => {
          // navigation to a successful page or login if the registration doesnt create session
          this.authService.setSession(res);
          this.toastService.info(4000, "successful registration", "proceed with login");
           this.router.navigate(['/dashboard']);
          // Or you could show a success message
          // this.showSuccessMessage('Account created successfully!');
        }),
        // Error handler:
        error: (err) => {
          // Set error message to display to user
          this.errorMessage = err.error?.message || 'Registration failed. Please try again.';
          // get the code and message from the error response if available
        
          
          this.toastService.error(4000, "Error", this.errorMessage);
          // Reset loading state
          this.isSubmitting = false;
        },
        // Complete handler (called after success or error)
        complete: () => {
          this.isSubmitting = false;
        }
      })
    } else {
      // If form is invalid, mark all fields as touched
      // This will trigger the error messages to display
      this.markFormGroupTouched(this.form);
      this.toastService.error(4000, "Invalid form", "Please correct the errors and try again.");
    }
  }
  /**
  * Helper method to mark all form controls as touched
  * This makes error messages appear even if user hasn't interacted with fields
  */
  private markFormGroupTouched(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }

  /**
  * Optional: Method to check if a specific field has an error
  * Usage in template: *ngIf="hasError('email', 'required')"
  */
  hasError(fieldName: string, errorType: string): boolean {
    const field = this.form.get(fieldName);
    return !!(field?.hasError(errorType) && field?.touched);
  }


  onFileSelected(event: any) {
    const file: File = event.target.files?.[0];

    if (!file) return;

    // 1. Validate type
    const allowedTypes = ['image/jpeg', 'image/png', 'image/webp'];
    if (!allowedTypes.includes(file.type)) {
      this.toastService.error(4000, "Invalid file", "Only JPG, PNG, WEBP allowed");
      this.resetForm();
      return;
    }

    // 2. Validate size (5MB)
    const maxSize = 5 * 1024 * 1024; // 5MB
    if (file.size > maxSize) {
      this.toastService.error(4000, "File too large", "Max size is 5MB");
      return;
    }

    // 3. Validate resolution (async)
    const img = new Image();
    const objectUrl = URL.createObjectURL(file);

    img.onload = () => {
      const width = img.width;
      const height = img.height;

      URL.revokeObjectURL(objectUrl);

      if (width > 5000 || height > 5000) {
        this.toastService.error(
          4000,
          "Invalid resolution",
          "Max resolution is 5000×5000"
        );
        return;
      }

      // If everything is valid → store file
      this.selectedFile = file;
    };

    img.onerror = () => {
      URL.revokeObjectURL(objectUrl);
      this.toastService.error(4000, "Invalid file", "Not a valid image");
    };

    img.src = objectUrl;
  }


  /**
   * Optional: Method to reset the form
   */
  resetForm() {
    this.form.reset();
    this.errorMessage = '';
  }

  ageRangeValidator(minAge: number, maxAge: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) return null;

      const dob = new Date(value);
      const today = new Date();
      const age = today.getFullYear() - dob.getFullYear();

      if (isNaN(dob.getTime())) return { invalidDate: true };
      if (age < minAge) return { tooYoung: { requiredAge: minAge } };
      if (age > maxAge) return { tooOld: { maxAge } };

      return null;
    };
  }

}
