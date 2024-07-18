import {booleanAttribute , Component , OnDestroy , OnInit , ViewChild} from '@angular/core';
import {Chess} from "chess.js";
import {ChessgameComponent} from "../chessgame/chessgame.component";
import {ChessplayService} from "../../service/chessplay.service";
import {liveGameDTO} from "../../data/liveGameDTO";
import {
  BehaviorSubject , endWith , finalize ,
  interval ,
  Observable ,
  retry ,
  startWith ,
  Subscription ,
  switchMap ,
  takeUntil , takeWhile ,
  timer
} from "rxjs";
import {Router} from "@angular/router";
import { Player } from '../../data/player';
import {PlayerService} from "../../service/player.service";
import {MatDialog} from "@angular/material/dialog";
import {ModificaDialogComponent} from "../../../options/components/modifica-dialog/modifica-dialog.component";
import {PopupResultComponent} from "../popup-result/popup-result.component";

@Component({
  selector: 'app-chessplay',
  templateUrl: './chessplay.component.html',
  styleUrl: './chessplay.component.scss'
})

export class ChessplayComponent implements OnInit, OnDestroy {
  protected readonly Math = Math;
  liveGame: liveGameDTO;
  timeInterval: Subscription;
  playerMe: Player;
  opponent: Player;

  playerMeTime: number;
  opponentTime: number;
  interval: any;

  game = new Chess();

  @ViewChild(ChessgameComponent) board: ChessgameComponent | undefined

  constructor(private backend: ChessplayService, private playerService: PlayerService, private matDialog: MatDialog, private router: Router) {
    this.playerMe = this.router.getCurrentNavigation()!.extras.state?.['playerMe'];
    this.liveGame = this.router.getCurrentNavigation()!.extras.state?.['game'];
    let oppUsrnm = this.opponent = this.router.getCurrentNavigation()!.extras.state?.['opponentUsername'];
    playerService.getPlayer(oppUsrnm).subscribe(
    res => { this.opponent = res }
    )
  }

  ngOnInit() {
    this.startPolling();
    const isWhite = this.liveGame.whitePlayer===this.playerMe.username;
    if (isWhite) {
      this.playerMeTime=this.liveGame.whiteTime/1000
      this.opponentTime=this.liveGame.blackTime/1000
    }
    else {
      this.playerMeTime=this.liveGame.blackTime/1000
      this.opponentTime=this.liveGame.whiteTime/1000
    }

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

  startPolling() {
    this.timeInterval = interval(400) //todo set 400
        .pipe(
            startWith(0),
            switchMap(() => this.backend.getGame()),
            retry(),
            takeWhile(response => response.result === null ),
            finalize(()=> {
              let title: string = "Sconfitta"
              if (this.liveGame.result === "draw") title = "Patta"
              else if ((this.liveGame.result === "white" && this.playerMe.username === this.liveGame.whitePlayer) ||
                  (this.liveGame.result === "black" && this.playerMe.username === this.liveGame.blackPlayer))
                title = "Vittoria"
              const dialogRef = this.matDialog.open(PopupResultComponent,{
                data: { title: title, type: this.liveGame.type },
              })
            })
        ).subscribe(res => {
              try {
                if (res) {
                  this.liveGame = res
                  this.game = new Chess(res.fens[res.fens.length-1])
                  this.board?.board.position(res.fens[res.fens.length-1])
                }
              } catch(err) {
                console.log('HTTP Error', err)
              }
          })
  }

  startGame(mode: string) {
    this.backend.startGame(mode).subscribe(
      res => {
        try {
          if (res) {
            this.liveGame = res;
            //creating from fen for testing purposes
            this.game = new Chess(res?.fens.pop());
          }
        } catch (err)  {
          console.log(err)
        }
      }
    )
    this.board?.refreshGame(this.liveGame.turn)
  }

  draw() {
    this.backend.draw().subscribe(
        res => {
          if (res) {
            this.liveGame = res;
          }
        }
    )
  }

  isDrawAsked() {
    return this.liveGame?.drawRequest || false;
  }

  ngOnDestroy() {
    clearInterval(this.interval);
  }

  deny() {
    this.backend.deny().subscribe()
  }

  getRating(player: Player) {
    switch (this.liveGame.type) {
      case "BULLET": return player.bulletPoints
      case "BLITZ": return player.blitzPoints
      case "RAPID": return player.rapidPoints
      default: return undefined;
    }
  }

  resign() {
    this.backend.resign().subscribe(
        res => {
          this.liveGame = res;
        }
    )
  }


  isGameOver(): boolean {
    return !!this.liveGame.result;
  }

  setGame ( data: any ) {
    this.liveGame = data;
  }
}

