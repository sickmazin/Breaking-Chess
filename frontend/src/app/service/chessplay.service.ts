import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import {liveGameDTO} from "../data/liveGameDTO";
import {Game} from "../data/game";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
  })
};

@Injectable({
  providedIn: 'root'
})
export class ChessplayService {
  gameUrl = 'http://localhost:8081/api/game/';  // URL to web api

  constructor(private http: HttpClient) {}

  startGame(mode: string) {
    return this.http.get<liveGameDTO | undefined>(this.gameUrl+'start',
      { headers: httpOptions.headers, params: { 'mode': mode }}
    )
  }

  makeMove(move: string) {
    return this.http.get<liveGameDTO>(this.gameUrl+'move',
      { headers: httpOptions.headers, params: { 'move': move }}
    )
  }

  getGame() {
    return this.http.get<liveGameDTO>(this.gameUrl+'get', httpOptions)
  }

  draw() {
    return this.http.get<liveGameDTO>(this.gameUrl+'draw', httpOptions)
  }

  abort() {
    return this.http.get<liveGameDTO>(this.gameUrl+'abort', httpOptions)
  }

  resign() {
    return this.http.get<liveGameDTO>(this.gameUrl+'resign', httpOptions)
  }

  deny() {
    return this.http.get<liveGameDTO>(this.gameUrl+'deny', httpOptions)
  }

  getGames() {
    return this.http.get<Game[]>(this.gameUrl+'games/get')
  }
}
