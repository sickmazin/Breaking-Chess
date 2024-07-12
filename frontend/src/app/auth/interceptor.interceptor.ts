import {
  HttpEvent ,
  HttpHandler ,
  HttpInterceptor ,
  HttpRequest
} from '@angular/common/http';
import {inject , Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";


@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let token = inject(AuthService).getToken()
    const acceptablePaths = ['/register','logout', "localhost:8080",'/token'];
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
