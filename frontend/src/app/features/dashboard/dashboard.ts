import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PostForm } from './post-form/post-form';


@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, PostForm],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {

}
