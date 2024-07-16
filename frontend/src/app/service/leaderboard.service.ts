import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Player} from "../data/player";
import {LEADERBOARD_URL} from "../support/constants";

@Injectable({
  providedIn: 'root'
})
export class LeaderboardService {
  constructor(private http: HttpClient) {
  }

  getLeaderboardByModality(modality: string) {
    return this.http.get<Player[]>(`${LEADERBOARD_URL}/${modality}`).toPromise();
  }
}
