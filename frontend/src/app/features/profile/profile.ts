import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, NavigationEnd } from '@angular/router';
import { UserDTO } from '../../types/user';
import { PostService } from '../../core/post/post-service';
import { PaginatedPosts, Post } from '../../types/post';
import { PostForm } from '../dashboard/post-form/post-form';
import { Authentication } from '../../core/authentication/auth/authentication';
import { filter } from 'rxjs/operators';
import { computed } from '@angular/core';



@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, PostForm ],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile implements OnInit {

  user = signal<UserDTO | undefined>(undefined);
  profileOwner = signal<boolean>(false);

  postToUpdate = signal<number | null>(null);

  // paginated related input:
  posts = signal<Post[]>([]);
  lastPage = signal(false);
  currentPage = signal(0);
  totalPages = signal(0);

  constructor(private postService: PostService, private auth: Authentication, private router: Router) { }

  ngOnInit(): void {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.handleNavigation();
    });

    // Initial load
    this.handleNavigation();
  }

  handleNavigation() {
    const loggedUser =  computed(() => this.auth.user());
    const state = window.history.state;

    if (state?.profileOwner) {
      this.user.set(loggedUser()?.user);
      this.profileOwner.set(true);
      console.log("pofiiiiiiiiiiiiiiiiile", this.profileOwner(), ' user:', this.user());

      
    } else if (state?.user) {
      this.user.set(state.user);
      this.profileOwner.set(false);
    } else {
      console.warn('No user passed and no logged-in user!');
    }

    this.loadPosts();
  }


  loadPosts() {
    let user = this.user()
    this.postService.fetchPosts(this.currentPage(), Number(user?.id)).subscribe((data: PaginatedPosts) => {
      if (data) {
        this.posts.set(data.content);
        this.lastPage.set(data.last);
        this.totalPages.set(data.totalPages);
      }
    })
  }

  toggleUpdate(postId: number) {
    this.postToUpdate.set(postId);
  }
}
