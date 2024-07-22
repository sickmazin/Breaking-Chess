import { Injectable } from '@angular/core';
import {Video} from "../data/video";
import {YOUTUBE_URL} from "../support/constants";

@Injectable({
  providedIn: 'root'
})
export class YoutubeService {

  constructor() { }

  getVideos() {
    return fetch(YOUTUBE_URL)
  }

}
