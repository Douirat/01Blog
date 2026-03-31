import { Component, OnInit, OnDestroy } from '@angular/core';
import { ToastService } from '../../../core/toast/toast-service';
import { Toast } from '../../../types/toast';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-toast-component',
  imports: [ToastService],
  templateUrl: './toast-component.html',
  styleUrl: './toast-component.scss',
})
export class ToastComponent implements OnInit, OnDestroy {

  toasts: Toast[] = [];
  sub!:Subscription;
  constructor(private toastService: ToastService){}

  ngOnInit(): void {
    this.sub = this.toastService.toast$.subscribe(toast => this.show(toast))
  }

  show(toast: Toast):void{
    this.toasts.push(toast);
    setTimeout(()=>this.removeToast(toast.id), toast.duration);
  }

  removeToast(id:string):void{
    this.toasts.filter(t => t.id != id);
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }
}
