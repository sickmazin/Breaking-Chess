import {Component, ViewChild} from '@angular/core';
import {Chess} from "chess.js";
import {ChessgameComponent} from "../chessgame/chessgame.component";
import {ChessplayService} from "./chessplay.service";

@Component({
  selector: 'app-chessplay',
  templateUrl: './chessplay.component.html',
  styleUrl: './chessplay.component.scss'
})

export class ChessplayComponent {
  game = new Chess()
  @ViewChild(ChessgameComponent) board: ChessgameComponent | undefined

  constructor(private backend: ChessplayService) {
    backend.getGame("first").subscribe(
      value => console.log(value)
    )
  }

  startGame() {
    this.game = new Chess();
    this.board?.startGame()
  }


  startBullet() {
    this.startGame()
  }
  startBlitz() {
    this.startGame()
  }
  startRapid() {
    this.startGame()
  }
}
