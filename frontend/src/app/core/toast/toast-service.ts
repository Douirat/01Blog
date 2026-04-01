import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Toast } from '../../types/toast';


@Injectable({
  providedIn: 'root',
})
export class ToastService {

  private subject = new Subject<Toast>();
  toast$ = this.subject.asObservable();

  success(duration:number, title: string, message?: string) {
    this.subject.next({ id: crypto.randomUUID(), type: 'success', title, message, duration});
  }

  error(duration:number, title: string, message?: string) {
    this.subject.next({ id: crypto.randomUUID(), type: 'error', title, message, duration});
  }

  warning(duration:number, title: string, message?: string) {
    this.subject.next({ id: crypto.randomUUID(), type: 'warning', title, message, duration });
  }


  info(duration:number, title: string, message?: string) {
    this.subject.next({ id: crypto.randomUUID(), type: 'info', title, message, duration });
  }

}
