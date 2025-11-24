import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostForm } from './post-form/post-form';
import { UserResponse } from '../../types/user';
import { Authentication } from '../../core/authentication/auth/authentication';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, PostForm],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit, OnDestroy {
  currentUser: UserResponse | null = null;
    private subscription?: Subscription;

  constructor(private authentication: Authentication){}

  ngOnInit(): void {
    this.subscription = this.authentication.currentUser$.subscribe(user =>{
      this.currentUser = user
       console.log('Current user changed:', user);
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe(); // prevent memory leaks
  }
}
