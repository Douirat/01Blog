import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Authentication } from '../../../core/authentication/auth/authentication';
import { LoginPayload } from '../types';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss'],
})
export class Login {
  form: FormGroup;
  isSubmitting = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: Authentication,
    private router: Router
  ) {
    this.form = this.fb.group({
      emailOrUsername: ['', [Validators.required, this.usernameOrEmailValidator]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  // Custom validator: either a valid email or any non-empty username
  usernameOrEmailValidator(control: AbstractControl) {
    const value = control.value;
    if (!value) return { required: true };

    // Check if it's a valid email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const isEmail = emailRegex.test(value);

    // Allow usernames: letters, numbers, underscores, 3-20 chars
    const usernameRegex = /^[a-zA-Z0-9_]{3,20}$/;
    const isUsername = usernameRegex.test(value);

    return isEmail || isUsername ? null : { invalidFormat: true };
  }

  hasError(fieldName: string, errorType: string) {
    const field = this.form.get(fieldName);
    return !!(field?.hasError(errorType) && field?.touched);
  }

  onSubmit() {
    Object.values(this.form.controls).forEach(c => c.markAsTouched());

    if (this.form.invalid) return;

    this.isSubmitting = true;
    this.errorMessage = '';
    const loginPayload: LoginPayload = this.form.value;

    this.authService.login(loginPayload).subscribe({
      next: res => {
        console.log('Login successful:', res);
        this.router.navigate(['/dashboard']);
      },
      error: err => {
        console.error('Login failed:', err);
        this.errorMessage = err.error?.message || 'Login failed. Check your credentials.';
        this.isSubmitting = false;
      },
      complete: () => (this.isSubmitting = false),
    });
  }
}
