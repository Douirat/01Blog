import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Toast } from '../../types/toast';


@Injectable({
  providedIn: 'root',
})
export class ToastService {

  private subject = new Subject<Toast>();
  toast$ = this.subject.asObservable();

  success(title: string, message?: string) {
    this.subject.next({ id: crypto.randomUUID(), type: 'success', title, message });
  }

  error(title: string, message?: string) {
    this.subject.next({ id: crypto.randomUUID(), type: 'error', title, message });
  }

  warning(title: string, message?: string) {
    this.subject.next({ id: crypto.randomUUID(), type: 'warning', title, message });
  }


  info(title: string, message?: string) {
    this.subject.next({ id: crypto.randomUUID(), type: 'info', title, message });
  }

}
