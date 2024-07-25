import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {ToastrService} from "ngx-toastr";
import {AuthService} from "./auth.service";

export const authGuard: CanActivateFn = (route, state) => {
  if (inject(AuthService).getToken()) {
    // User is logged in, so return true
    return true;
  }else {
    console.log('no token');
    // User is not logged in, redirect to login page and return false
    inject(ToastrService).error("Non puoi accedere all\'homepage senza l\'accesso");
    inject(Router).navigate(['/signUp']).then(r => false);
    return false;
  }
};
export const authSignPage: CanActivateFn = (route, state) => {
  if (inject(AuthService).getToken()) {
    console.log("Sei connesso")
    // User is logged in, so return true
    inject(Router).navigate(['/homepage']).then(r => false);
    return false;
  }else {
    console.log("NOn sei connesso")
    // User is not logged in, so he can stay at login page
    return true;
  }
};