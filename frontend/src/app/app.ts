import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {Header} from './public/header/header'
import { ToastComponent } from './public/toast/toast-component/toast-component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Header,ToastComponent],
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('01blog');
}
