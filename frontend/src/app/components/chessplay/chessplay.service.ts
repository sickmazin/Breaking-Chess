import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { Observable, catchError, map } from 'rxjs';
import {liveGame} from "../../data/liveGame";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
  })
};

@Injectable({
  providedIn: 'root'
})
export class ChessplayService {
  gameUrl = 'http://localhost:8081/game/';  // URL to web api

  constructor(private http: HttpClient) {}

  startGame(nickname:string, mode: string) {
    let lg = null
    this.http.post<liveGame>(this.gameUrl+'move/'+nickname+"/"+mode, httpOptions).subscribe(
      value => lg = value
    )
    return lg;
  }

  makeMove(gameid:string, nickname:string, move: string) {
    let lg = null
    this.http.post<liveGame>(this.gameUrl+'move/'+nickname+'/'+gameid+'/'+move, httpOptions).subscribe(
      value => lg = value
    )
    return lg;
  }

  getGame(nickname: string) {
    return this.http.get<liveGame>(this.gameUrl+'get/'+nickname, httpOptions)
  }
}
