import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";

export const authGuard: CanActivateFn = (route, state) => {
  if (localStorage.getItem('token')) {
    console.log('yes token');
    // User is logged in, so return true
    return true;
  }else {
    console.log('no token');
    // User is not logged in, redirect to login page and return false
    inject(Router).navigate(['/signUp']).then(r => false);
    return false;
  }
};
export const authSignPage: CanActivateFn = (route, state) => {
  if (localStorage.getItem('token')) {
    // User is logged in, so return true
    console.log("IL TOKEN C'è")
    inject(Router).navigate(['/homepage']).then(r => false);
    return false;
  }else {
    // User is not logged in, so he can stay at login page
    return true;
  }
};