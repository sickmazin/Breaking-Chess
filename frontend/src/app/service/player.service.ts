import { Injectable } from '@angular/core';
import {HttpClient , HttpParams} from "@angular/common/http";
import {OPTIONS_URL} from "../support/constants";

@Injectable({
  providedIn: 'root'
})
export class PlayerService {

  constructor(private http:HttpClient) { }

  changeFirstName ( inputValue: string ) {
    const params = new HttpParams().set('firstName', inputValue);
    return this.http.put(OPTIONS_URL+"/changeFirstName", null, { params }).toPromise();
  }

  changeNickname ( inputValue: string ) {
    const params = new HttpParams().set('nickname', inputValue);
    return this.http.put(OPTIONS_URL+"/changeNickname", null, { params }).toPromise();
  }

  changeLastName ( inputValue: string ) {
    const params = new HttpParams().set('lastName', inputValue);
    return this.http.put(OPTIONS_URL+"/changeLastName", null, { params }).toPromise();
  }

  changeEmail ( inputValue: string ) {
    const params = new HttpParams().set('email', inputValue);
    return this.http.put(OPTIONS_URL+"/changeEmail", null, { params }).toPromise();
  }
  changeAvatar ( inputValue: string ) {
    const params = new HttpParams().set('avatar', inputValue);
    return this.http.put(OPTIONS_URL+"/changeAvatar", null, { params }).toPromise();
  }

  getStatFor ( modality: string ) {
    return this.http.get(OPTIONS_URL+"/statisticFor"+modality);
  }
}
