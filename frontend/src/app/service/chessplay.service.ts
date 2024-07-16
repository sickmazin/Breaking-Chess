import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import {liveGameDTO} from "../data/liveGameDTO";
import {Game} from "../data/game";
import {GAMES_URL} from "../support/constants";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
  })
};

@Injectable({
  providedIn: 'root'
})
export class ChessplayService {

  constructor(private http: HttpClient) {}

  startGame(mode: string) {
    return this.http.get<liveGameDTO | undefined>(GAMES_URL+'start',
      { headers: httpOptions.headers, params: { 'mode': mode }}
    )
  }

  makeMove(move: string) {
    return this.http.get<liveGameDTO>(GAMES_URL+'move',
      { headers: httpOptions.headers, params: { 'move': move }}
    )
  }

  getGame() {
    return this.http.get<liveGameDTO>(GAMES_URL+'get', httpOptions)
  }

  draw() {
    return this.http.get<liveGameDTO>(GAMES_URL+'draw', httpOptions)
  }

  abort() {
    return this.http.get<liveGameDTO>(GAMES_URL+'abort', httpOptions)
  }

  resign() {
    return this.http.get<liveGameDTO>(GAMES_URL+'resign', httpOptions)
  }

  deny() {
    return this.http.get<liveGameDTO>(GAMES_URL+'deny', httpOptions)
  }

  getGames() {
    return this.http.get<Game[]>(GAMES_URL+'games/get')
  }
}
