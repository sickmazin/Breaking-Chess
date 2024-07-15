import {Component , Input} from '@angular/core';
import {Player} from "../../../app/data/player";
import {Stat} from "../../../app/data/Stat";
import {PlayerService} from "../../../app/service/player.service";

@Component({
  selector: 'app-chess-board-page',
  templateUrl: './chess-board-page.component.html',
  styleUrl: './chess-board-page.component.scss'
})
export class ChessBoardPageComponent {
    private stat: Stat;
  constructor (private playerService: PlayerService) {
  }
  @Input() player: Player;

}
