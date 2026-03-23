import { Injectable } from '@angular/core';
import { signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Store {
  private user = signal<any | null>(null);

  readonly _user = this.user.asReadonly();

  set(user: any) {
    this.user.set(user);
  }

  get():any{
    return this._user();
  }

  clear() {
    this.user.set(null);
  }
}
// TODO: user the single tone to inhance authentication later: