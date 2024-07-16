import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {ChessplayService} from "../../service/chessplay.service";
import {liveGameDTO} from "../../data/liveGameDTO";
import {interval, retry, startWith, Subscription, switchMap} from "rxjs";
import {Player} from "../../data/player";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-matchmaking',
  templateUrl: './matchmaking.component.html',
  styleUrl: './matchmaking.component.scss'
})
export class MatchmakingComponent implements OnInit, OnDestroy {
  liveGame: liveGameDTO;
  timeInterval: Subscription;
  player: Player

  constructor(private backend: ChessplayService, private router: Router, private toastr:ToastrService) {
    this.liveGame = this.router.getCurrentNavigation()!.extras.state?.['game'];
    this.player = this.router.getCurrentNavigation()!.extras.state?.['player'];
  }


  cancelSearch() {
    this.backend.abort()
    console.log("CANCELLO PARTITA AVVIATA")
    this.router.navigate(['/homepage'])
  }

  ngOnInit(): void {
    let navigate = () => {
      let otherP = (this.liveGame.whitePlayer===this.player.username)? this.liveGame.blackPlayer: this.liveGame.whitePlayer
      this.router.navigate(['/game'], {state:{game:this.liveGame, playerMe: this.player, opponentUsername: otherP}})
      console.log(this.liveGame)
      console.log(this.player)
      console.log(otherP)
    }
    if (this.liveGame && this.liveGame.turn!="") {
      console.log()
      navigate ();
    }
    //fai polling per conoscere lo stato della richiesta di avvio partita se il match non è stato trovato
    this.timeInterval = interval(300) //RIMETTERE A 300 todo
      .pipe(
        startWith(0),
        switchMap(() => this.backend.getGame()),
        retry()
      ).subscribe(res => {
          this.liveGame = res
          if (res && this.liveGame.turn!="") {
            navigate()
            this.toastr.success("avversario trovato!")
          }

        },
        err => console.log('HTTP Error', err)
      )
  }

  ngOnDestroy(): void {
    this.timeInterval.unsubscribe()
  }
}
