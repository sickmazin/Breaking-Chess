import {Component , Input , OnInit} from '@angular/core';
import {Game} from "../../data/game";
import {Player} from "../../data/player";

@Component({
  selector: 'app-game-display',
  templateUrl: './game-display.component.html',
  styleUrl: './game-display.component.scss'
})
export class GameDisplayComponent{

  @Input() game: Game;
  @Input() playerUsername: string;
  getResult() {
    if (this.game.result== "draw") { return "DRAW"}
    else {
      if ((this.game.result == "white" && this.playerUsername == this.game.whitePlayer.username)||this.game.result == "black" && this.playerUsername==this.game.blackPlayer.username) {
        return "WIN"
      }
      else return "LOSE"
    }
  }

  formatDates( date: Date ) {
    date = new Date( date );
    const day= date.getDay()
    const month = date.getMonth() + 1; // I mesi in JavaScript sono indicizzati da 0 a 11
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  }
}
