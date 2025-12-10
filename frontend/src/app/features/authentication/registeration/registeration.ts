import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Authentication } from '../../../core/authentication/auth/authentication'; // adjust to your path
import { User } from '../../../types/user'; // adjust to your path


@Component({
  selector: 'app-registeration',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './registeration.html',
  styleUrls: ['./registeration.scss'], 
})
export class Registeration {
  form: FormGroup;
  isSubmitting = false; // Track the submission state.
  errorMessage = ''; // Store error message.

  

  constructor(
    private fb: FormBuilder,
    private authService: Authentication, // Inject the authentication service.
    private router: Router // Inject the router.
  ) {
    // Initializing the form with validation rules:
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
      avatar: [''], // Optional field.
      nickname: ['', Validators.required],
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
      // const newUser: User = this.form.value;
      // console.log("The new created user is: " , newUser);
      const formData = new FormData();
formData.append("email", this.form.value.email);
formData.append("password", this.form.value.password);
formData.append("firstName", this.form.value.firstName);
formData.append("lastName", this.form.value.lastName);
formData.append("nickname", this.form.value.nickname);
formData.append("dateOfBirth", this.form.value.dateOfBirth);

if (this.selectedFile) {
  formData.append("avatar", this.selectedFile);
}

      // call the authentication service to register the user:
      this.authService.register(formData).subscribe({
        // Success handler:
        next: (res => {
          console.log("successful registration: ", res);
          // navigation to a successful page or login if the registration doesnt create session
          this.router.navigate(['/login'])
          // Or you could show a success message
          // this.showSuccessMessage('Account created successfully!');
        }),
        // Error handler:
        error: (err) => {
          console.error('Registration error:', err);

          // Set error message to display to user
          this.errorMessage = err.error?.message || 'Registration failed. Please try again.';

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
      console.log('Form is invalid');
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

  /**
   * Optional: Method to reset the form
   */
  resetForm() {
    this.form.reset();
    this.errorMessage = '';
  }
}
