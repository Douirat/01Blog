import { Injectable } from '@angular/core';
import { signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Store {
  private _user = signal<any | null>(null);

  readonly user = this._user.asReadonly();

  set(user: any) {
    this._user.set(user);
  }

  clear() {
    this._user.set(null);
  }
}
