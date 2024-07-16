import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Chess} from "chess.js";
import {ChessgameComponent} from "../chessgame/chessgame.component";
import {ChessplayService} from "../../service/chessplay.service";
import {liveGameDTO} from "../../data/liveGameDTO";
import {interval, retry, Subscription, switchMap, timer} from "rxjs";
import {Router} from "@angular/router";
import { Player } from '../../data/player';
import {PlayerService} from "../../service/player.service";

@Component({
  selector: 'app-chessplay',
  templateUrl: './chessplay.component.html',
  styleUrl: './chessplay.component.scss'
})

export class ChessplayComponent implements OnInit, OnDestroy {
  liveGame: liveGameDTO;
  liveGameSubscription: Subscription;
  timeInterval: Subscription;
  playerMe: Player;
  opponent: Player;

  playerMeTime: number;
  opponentTime: number;
  interval: any;

  game = new Chess();

  @ViewChild(ChessgameComponent) board: ChessgameComponent | undefined

  constructor(private backend: ChessplayService, private playerService: PlayerService, private router: Router) {
    this.playerMe = this.router.getCurrentNavigation()!.extras.state?.['playerMe'];
    this.liveGame = this.router.getCurrentNavigation()!.extras.state?.['game'];

    let oppUsrnm = this.opponent = this.router.getCurrentNavigation()!.extras.state?.['opponentUsername'];
    playerService.getPlayer(oppUsrnm).subscribe(
      res => { this.opponent = res }
    )

    this.playerMeTime = this.liveGame.whiteTime/1000
    this.opponentTime = this.liveGame.whiteTime/1000
  }

  ngOnInit() {
    console.log("PLAYER ME")
    console.log(this.playerMe)
    this.timeInterval = interval(40000) //TODO
      .pipe(
        switchMap(() => this.backend.getGame()),
        retry(2)
      ).subscribe(res => {
          this.liveGame = res
          this.game = new Chess(res.fen.at(res.fen.length-1))
        },
        err => console.log('HTTP Error', err)
      )

    this.interval = setInterval(() => {
        if (this.playerMeTime>0 || this.opponentTime>0) {
          if (this.liveGame.turn === this.playerMe.username)
            this.playerMeTime--;
          else this.opponentTime--;
        }
      },
      1000
    )
  }

  startGame(mode: string) {
    this.backend.startGame(mode).subscribe(
      res => {
        try {
          if (res) {
            this.liveGame = res;
            //creating from fen for testing purposes
            this.game = new Chess(res?.fen.at(res?.fen.length-1));
          }
        } catch (err)  {
          console.log(err)
        }
      }
    )
    this.board?.startGame(this.liveGame.turn)
  }

  startBullet() {
    this.startGame("BULLET")
  }
  startBlitz() {
    this.startGame("BLITZ")
  }
  startRapid() {
    this.startGame("RAPID")
  }

  draw() {
    this.backend.draw()
  }

  isDrawAsked() {
    return this.liveGame?.drawRequest || false;
  }

  ngOnDestroy() {
    this.liveGameSubscription.unsubscribe()
    this.timeInterval.unsubscribe()
    clearInterval(this.interval);
  }

  deny() {
    this.backend.deny()
  }

  getRating(player: Player) {
    switch (this.liveGame.mode) {
      case "BULLET": return player.bulletPoints
      case "BLITZ": return player.blitzPoints
      case "RAPID": return player.rapidPoints
      default: return undefined;
    }
  }
}
