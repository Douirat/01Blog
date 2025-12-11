import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UserDTO } from '../../types/user';

@Component({
  selector: 'app-profile',
  imports: [CommonModule],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile implements OnInit {
  user = signal<UserDTO | null>(null);
  constructor(private router: Router) { }
ngOnInit(): void {
  const state = window.history.state;
  this.user.set(state?.user ?? null);
  console.log("--------->User data:", this.user());

  if (!this.user()) {
    console.warn('No user passed!');
  }
}
}
