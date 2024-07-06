import {
  HTTP_INTERCEPTORS,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import {Injectable, Provider} from "@angular/core";
import {Observable} from "rxjs";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler):
      Observable<HttpEvent<any>> {
          const token= localStorage.getItem("token")
          if(!token){ return next.handle(req) }
          const authorizedRequest = req.clone({ headers: req.headers.set('Authorization', `Bearer ${token}`), });
          return next.handle(authorizedRequest);
  }
}
export const AuthInterceptorProvider: Provider = { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true };