import { Component } from '@angular/core';
import {Player} from "../../data/player";
import {Game} from "../../data/game";
import {Video} from "../../data/video";

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent {
  leaderboard: Player[];
  isLightTheme: boolean;
  books: any;
  games: Game[];
  videos:Video[]; //this.youtubeService.getVideos()
  modality: number;
  player: Player;
  leardBoardModality: string="blitz";
  modalityEloPoints: string;

  showClassificaByModality( rapid: string ) {
    //TODO
  }

  openOptionsPage() {
    //TODO

  }

  logout() {
    //TODO

  }

  openFriendsPage() {
    //TODO
  }

  changeTheme() {
    //TODO
  }

  openLink( linkVideo: string ) {
    //TODO
  }

  postGame( rapid: string ) {
    //TODO
  }

  changeModalityEloPoints() {
    //TODO
  }

  getPuntiEloByModality() {
    return "";//TODO
  }
}
