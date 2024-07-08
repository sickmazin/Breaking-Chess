import {Injectable} from '@angular/core';
import {HttpClient , HttpHeaders , HttpParams} from "@angular/common/http";
import {catchError , firstValueFrom , map , Observable , switchMap , timer} from "rxjs";
import {
  ADDRESS_AUTHENTICATION_SERVER ,
  CLIENT_ID ,
  CLIENT_SECRET ,
  CREATE_USER ,
  LOGIN_URL ,
  REQUEST_LOGIN
} from "../support/constants";
import {jwtDecode} from "jwt-decode";
import {PlayerService} from "../service/player.service";
import {Player} from "../data/player";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient,
              private playerService: PlayerService) { }

  private startTokenRefresh(refreshToken: string) {
    timer(0, 500 * 1000).pipe(
        switchMap(() => this.refreshToken(refreshToken))
    ).subscribe();
  }
  private refreshToken(refreshToken: string): Observable<any> {
    const headers = new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded'});
    const body = new HttpParams()
        .set('grant_type', 'refresh_token')
        .set('client_id',CLIENT_ID)
        .set('client_secret',CLIENT_SECRET)
        .set('refresh_token',refreshToken);
    return this.http.post(ADDRESS_AUTHENTICATION_SERVER + REQUEST_LOGIN, body,{headers}).pipe(
        map((response: any) => {
          localStorage.setItem('token', response.access_token);
          return jwtDecode(response.access_token);
        }),
        catchError(error => {
          console.log('Error refreshing token:', error);
          return [];
        })
    );
  }


  async signInWithEmailAndPassword(user: { password: any; username: any }):Promise<Player> {
    try {
      await this.getAndSetToken(user);
      console.log("Chiamo backend")
      const resp= await firstValueFrom(this.http.get<Player>(LOGIN_URL));
      return resp;
    } catch (error: any) {
      throw new Error(error  || 'Error during sign in');
    }
  }
  private getAndSetToken(user: { password: any; username: any }): Promise<void> {
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
          console.log("SETTING TOKEN");
          localStorage.setItem('token', response.access_token);
          localStorage.setItem('refresh_token', response.refresh_token);
          this.startTokenRefresh(response.refresh_token);
          resolve();
        },
        error: err => {
          reject(err);
        }
      });
    });
  }
  async signUpWithEmailAndPassword( player: {
    firstName: any;
    lastName: any;
    password: any;
    avatar: string;
    email: any;
    username: string
  } ) {
    const headers = new HttpHeaders({'Content-Type': 'application/json'});
    try {
      const response = await firstValueFrom(this.http.post(CREATE_USER , player , {headers}));
      await this.getAndSetToken(player);
      return response as Player;
    } catch (error: any) {
      throw new Error(error.error.errorMessage || 'Error during sign up');
    }
  }

  async loginWithToken() {
    try {
      const refresh_token=localStorage.getItem('refresh_token');
      if (refresh_token!=null){
        console.log("Refreshing token");
        this.startTokenRefresh(refresh_token)
      }
      return await firstValueFrom (this.http.get<Player> (LOGIN_URL));
    } catch (error: any) {
      throw new Error (error || 'Error during sign in');
    }
  }
}
