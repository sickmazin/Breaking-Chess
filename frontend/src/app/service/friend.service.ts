import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import {FRIEND_URL} from "../support/constants";
import {Player} from "../data/player";

const httpOptions = {
    headers: new HttpHeaders({
        'Content-Type':  'application/json',
    })
};

@Injectable({
    providedIn: 'root'
})
export class FriendService {
    constructor ( private http: HttpClient ) {
    }

    getFriends () {
        return this.http.get<Player[]> ( FRIEND_URL + 'get' , httpOptions )
    }

    getPendingRequests() {
        return this.http.get<Player[]> ( FRIEND_URL + 'request' , httpOptions )
    }

    askRequest ( player: string ) {
        return this.http.post ( FRIEND_URL + 'ask' , null,
            {headers: httpOptions.headers , params: { 'player' : player }}
        )
    }

    delete ( player: string ) {
        return this.http.delete ( FRIEND_URL + 'delete' ,
            {headers: httpOptions.headers , params: { 'player': player }}
        )
    }

    acceptRequest ( player: string ) {
        return this.http.put ( FRIEND_URL + 'accept' , null,
            {headers: httpOptions.headers , params: { 'player': player }}
        )
    }
}