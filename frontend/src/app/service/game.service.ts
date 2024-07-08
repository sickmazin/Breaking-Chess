import { Injectable } from '@angular/core';
import {Game} from "../data/game";
import {GAMES_URL} from "../support/constants";
import {HttpClient} from "@angular/common/http";
import {Player} from "../data/player";

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor(private http: HttpClient) { }

  deleteGame( id: string ) {

  }

  getGames() {
    return this.http.get<Game[]>(`${GAMES_URL}/listOfGame`).toPromise();
  }

  postGame( modality: any , player: Player ) {
    return this.http.post<Game>(`${GAMES_URL}/wantToPlay/${modality}`, player).toPromise()
  }
}
