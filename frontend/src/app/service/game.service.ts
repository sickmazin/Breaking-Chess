import { Injectable } from '@angular/core';
import {Game} from "../data/game";
import {GAMES_URL} from "../support/constants";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor(private http: HttpClient) { }

  abortGame( id: string ) {
    //TODO
  }

  getGames() {
    return this.http.get<Game[]>(`${GAMES_URL}/listOfGame`).toPromise();
  }

}
