import {Injectable} from '@angular/core';
import {HttpClient , HttpHeaders , HttpParams} from "@angular/common/http";
import {catchError , firstValueFrom , map , Observable , switchMap , timer} from "rxjs";
import {
  ACCOUNT_URL ,
  ADDRESS_AUTHENTICATION_SERVER ,
  CLIENT_ID ,
  CLIENT_SECRET ,
  CREATE_USER ,
  LOGIN_URL ,
  REQUEST_LOGIN , REQUEST_LOGOUT
} from "../support/constants";
import {jwtDecode} from "jwt-decode";
import {PlayerService} from "../service/player.service";
import {Player} from "../data/player";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {error} from "jquery";
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient,
              private playerService: PlayerService,
              private router:Router,
              private toastr: ToastrService,
              private jwtHelper: JwtHelperService
  ) { }

  private startTokenRefresh ( refreshToken: string , saveToken: boolean ) {
    timer(0, 500 * 1000).pipe(
        switchMap(() => this.refreshToken(refreshToken,saveToken))
    ).subscribe();
  }
  refreshToken ( refreshToken: string , saveToken: boolean ): Observable<any> {
    const headers = new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded'});
    const body = new HttpParams()
        .set('grant_type', 'refresh_token')
        .set('client_id',CLIENT_ID)
        .set('client_secret',CLIENT_SECRET)
        .set('refresh_token',refreshToken);
    return this.http.post(ADDRESS_AUTHENTICATION_SERVER + REQUEST_LOGIN, body,{headers}).pipe(
        map((response: any) => {
          if(saveToken) localStorage.setItem('token', response.access_token);
          else sessionStorage.setItem('token', response.access_token);
          return jwtDecode(response.access_token);
        }),
        catchError(error => {
          console.log('Error refreshing token:', error);
          return [];
        })
    );
  }
  async signInWithEmailAndPassword ( user: { password: any; username: any } , checked: boolean ):Promise<Player> {
    await this.getAndSetToken (user , checked).then(
        r => {},
        error=>{
          this.toastr.error(error.error.error_description)
          return;
        });
    return await firstValueFrom (this.http.get<Player> (LOGIN_URL));
  }
  private getAndSetToken ( user: { password: any; username: any } , saveToken: boolean ): Promise<void> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded'
    });
    const body = new URLSearchParams();
    body.set('grant_type', 'password');
    body.set('client_id', CLIENT_ID);
    body.set('client_secret', CLIENT_SECRET);
    body.set('username', user.username);
    body.set('password', user.password);

    return new Promise((resolve, reject) => {
      this.http.post(ADDRESS_AUTHENTICATION_SERVER + REQUEST_LOGIN, body, { headers }).subscribe({
        next: (response: any) => {
          if(saveToken){
            localStorage.setItem('token', response.access_token);
            localStorage.setItem('refresh_token', response.refresh_token);
          }else {
            sessionStorage.setItem('token', response.access_token);
            sessionStorage.setItem('refresh_token', response.refresh_token);
          }
          this.startTokenRefresh(response.refresh_token,saveToken);
          resolve();
        },
        error: err => {
          reject(err);
        }
      });
    });
  }
  async signUpWithEmailAndPassword ( player: {
    firstName: any;
    lastName: any;
    password: any;
    avatar: string;
    email: any;
    username: string
  } , checked: boolean ) {
    const headers = new HttpHeaders({'Content-Type': 'application/json'});
    try {
      const response = await firstValueFrom(this.http.post(CREATE_USER , player , {headers}));
      await this.getAndSetToken(player,checked);
      return response as Player;
    } catch (error: any) {
      throw new Error(error.error.errorMessage || 'Error during sign up');
    }
  }
  async loginWithToken() {
    try {
      return await firstValueFrom (this.http.get<Player> (LOGIN_URL));
    } catch (error: any) {
      throw new Error (error || 'Error during sign in');
    }
  }
  logout() {
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded'
    });
    const refresh_token=localStorage.getItem('refresh_token')!=null?localStorage.getItem('refresh_token'):sessionStorage.getItem('refresh_token');

    const body = new HttpParams()
        .set('client_id', CLIENT_ID)
        .set('client_secret', CLIENT_SECRET)
        .set('refresh_token', refresh_token as string);

    return this.http.post(ADDRESS_AUTHENTICATION_SERVER+REQUEST_LOGOUT, body.toString(), { headers }).subscribe({
        next: value => {
          this.toastr.success("Logout effettuato con successo!")
          localStorage.removeItem('token')
          localStorage.removeItem('refresh_token')
          sessionStorage.removeItem('token')
          sessionStorage.removeItem('refresh_token')
          this.router.navigate(['signUp'])
        },
        error: err => {
          this.toastr.error("PROBLEMI NEL LOGOUT")
          throw new Error(err)
        }
    });
  }

  // @ts-ignore
  getToken () {
    let token= localStorage.getItem ("token")!=null? localStorage.getItem ("token"):sessionStorage.getItem("token");
    let refresh_token= localStorage.getItem ("refresh_token")!=null? localStorage.getItem ("refresh_token"):sessionStorage.getItem("refresh_token");
    let  saveTokenOnLocalStorage = localStorage.getItem ("token") != null;
    if(this.jwtHelper.isTokenExpired(token) && token!=null && refresh_token!=null ){
      this.refreshToken(refresh_token,saveTokenOnLocalStorage)
      console.log("era expired")
      return this.getToken() as string;
    }
    return token;
  }
}
