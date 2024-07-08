import {Component , Input} from '@angular/core';
import {Player} from "../../data/player";

@Component({
  selector: 'app-leaderboard-element',
  templateUrl: './leaderboard-element.component.html',
  styleUrl: './leaderboard-element.component.scss'
})
export class LeaderboardElementComponent {
  @Input() position: number;
  @Input() player: Player;
  @Input() modality:string;

  getEloPoints() {
    switch (this.modality){
      case 'bullet': return this.player.bulletPoints;
      case 'blitz': return this.player.blitzPoints;
      case 'rapid': return this.player.rapidPoints;
      default: return 0;
    }
  }
}
