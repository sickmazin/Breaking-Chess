import {Component , Input} from '@angular/core';
import {Game} from "../../data/game";

@Component({
  selector: 'app-game-display',
  templateUrl: './game-display.component.html',
  styleUrl: './game-display.component.scss'
})
export class GameDisplayComponent {
  @Input() game: Game;
}
