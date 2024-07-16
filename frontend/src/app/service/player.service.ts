import { Injectable } from '@angular/core';
import {HttpHeaders} from "@angular/common/http";
import { HttpClient } from '@angular/common/http';
import {Player} from "../data/player";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
  })
};

@Injectable({
  providedIn: 'root'
})
export class PlayerService {
  playerURL = "http://localhost:8081/player/"

  constructor(private http: HttpClient) { }

  getPlayer(username : string) {
    return this.http.get<Player>(this.playerURL+'get/'+username);
  }
}
