import {
  HttpEvent ,
  HttpHandler ,
  HttpInterceptor ,
  HttpRequest
} from '@angular/common/http';
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";


@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token= localStorage.getItem("token")
    const acceptablePaths = ['/register', '/login', "localhost:8080"];
    const  url = request.url;
    const isFreePathMatch = acceptablePaths.some((path) =>
        url.includes(path)
    );
    if(!token || isFreePathMatch){ return next.handle(request) }
    else {
      const authorizedRequest = request.clone({ headers: request.headers.set('Authorization', `Bearer ${token}`), });
      return next.handle(authorizedRequest);
    }
  }
}
