import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError , map , Observable , switchMap , timer} from "rxjs";
import {ADDRESS_AUTHENTICATION_SERVER , CLIENT_ID , CLIENT_SECRET , REQUEST_LOGIN} from "../support/constants";
import {jwtDecode } from "jwt-decode";
import {PlayerService} from "../service/player.service";

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
    return this.http.post(ADDRESS_AUTHENTICATION_SERVER + REQUEST_LOGIN, {
      grant_type: 'refresh_token',
      client_id: CLIENT_ID,
      client_secret: CLIENT_SECRET,
      refresh_token: refreshToken
    }).pipe(
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

  async signInWithEmailAndPassword(user: { password: any; username: any }) {

    await this.getAndSetToken(user)
    return this.playerService.getInfo();
  }

  private getAndSetToken(user: { password: any; username: any }) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded'
    });
    const body = new URLSearchParams();
    body.set('grant_type', 'password');
    body.set('client_id', CLIENT_ID);
    body.set('client_secret', CLIENT_SECRET);
    body.set('username', user.username);
    body.set('password', user.password);
    this.http.post(ADDRESS_AUTHENTICATION_SERVER + REQUEST_LOGIN , body , {headers}).subscribe({
      next: (response: any) => {
        console.log("SETTING TOKEN")
        localStorage.setItem('token' , response.access_token);
        this.startTokenRefresh(response.refresh_token);
      } ,
      error: err => {
        throw new Error(err)
      }
    })
  }
}
