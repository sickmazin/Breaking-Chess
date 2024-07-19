import {Component , Inject} from '@angular/core';
import {MAT_DIALOG_DATA , MatDialogRef} from "@angular/material/dialog";
import {ToastrService} from "ngx-toastr";
import {PlayerService} from "../../service/player.service";
import {Router} from "@angular/router";
import {ChessplayService} from "../../service/chessplay.service";
import {data} from "jquery";
import { Player } from '../../data/player';
import {liveGameDTO} from "../../data/liveGameDTO";

@Component({
  selector: 'app-popup-result',
  templateUrl: './popup-result.component.html',
  styleUrl: './popup-result.component.scss'
})

export class PopupResultComponent {
  text: string;
  constructor (@Inject(MAT_DIALOG_DATA) public data: {player: Player, title: string, type:string},
               private router: Router,
               private playerService: PlayerService,
               private chessplayService: ChessplayService,
               private toastr: ToastrService,
               public dialogRef: MatDialogRef<PopupResultComponent>,
  ) {
    //playerService.getPlayer(this.data.player.username).subscribe(
    //    res => {}
    //)
  }

  startNewGame() {
    this.chessplayService.startGame ( this.data.type ).toPromise ().then (
        ( response: liveGameDTO | undefined ) => {
          this.router.navigate ( ['/matchmaking'] , {
            state: {
              player: this.data.player ,
              game: response
            }
          } ).then ( r => {
                this.toastr.success ( "Matchmaking iniziato!" )
              } ,
              err => {
                this.toastr.error ( err )
              }
          )
        })
    this.dialogRef.close()
  }

  goHome() {
    this.router.navigate(['/homepage'])
    this.dialogRef.close()
  }

}
