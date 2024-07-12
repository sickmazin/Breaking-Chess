import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Video} from "../data/video";
import {YOUTUBE_URL} from "../support/constants";

@Injectable({
  providedIn: 'root'
})
export class YoutubeService {

  constructor(private http: HttpClient) { }
  videos: Array<Video> = [];

  getVideos() {
    fetch(YOUTUBE_URL).then(response => response.json()).then(
        data => {
          for (const video of data.items) {
            const id=video.snippet.resourceId.videoId;
            const title= video.snippet.title;
            const linkImg= video.snippet.thumbnails.maxres.url;
            const v= new Video(id,title,linkImg);
            this.videos.push(v);
          }
        },
        error=>{
            this.videos=[]
          console.log(error);
        }
    )
    return this.videos;
  }

}
