import { Component } from '@angular/core';
import {Game} from "../../data/game";
import {Router} from "@angular/router";
import {GameService} from "../../service/game.service";

@Component({
  selector: 'app-matchmaking',
  templateUrl: './matchmaking.component.html',
  styleUrl: './matchmaking.component.scss'
})
export class MatchmakingComponent {
  game: Game;

  constructor(private gameService: GameService, private router: Router,) {
    this.game = this.router.getCurrentNavigation()!.extras.state?.['game'];
  }
  cancelSearch() {
    console.log("CANCELLO PARTITA AVVIATA")
    this.gameService.deleteGame(this.game.id)
    this.router.navigate(['/homepage'])
  }
}
