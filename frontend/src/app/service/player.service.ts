import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PlayerService {

  constructor() { }

  getInfo() {
    return Promise.resolve(undefined);
  }
}
