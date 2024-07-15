import {Component , Input} from '@angular/core';
import {Player} from "../../../app/data/player";
import {MatDialog } from "@angular/material/dialog";
import {ModificaDialogComponent} from "../modifica-dialog/modifica-dialog.component";
import {AvatarComponent} from "../../../app/components/avatar/avatar.component";
import {PlayerService} from "../../../app/service/player.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrl: './user-page.component.scss'
})
export class UserPageComponent {
  constructor ( private dialog: MatDialog,private playerService: PlayerService,private toastr: ToastrService) {
  }
  @Input() player: Player;

  clickedModifica ( type: string ) {
    switch (type) {
      case 'Nome': {
        const dialogRef= this.dialog.open (ModificaDialogComponent , {
          data: {
            text: 'Qui puoi modificare il tuo nome personale' ,
            type: type
          } ,
        })
        dialogRef.afterClosed().subscribe({
          next: value => {
            if (value!=null||value!=undefined) this.player=value as Player;
          }
        })
        break;
      }
      case 'Nickname':{
        const dialogRef=this.dialog.open(ModificaDialogComponent,{
          data: { text: 'Ricorda: il nickname non deve esser già utilizzato e non più lungo di 20 caratteri', type: type },
        })
        dialogRef.afterClosed().subscribe({
          next: value => {
            if (value!=null||value!=undefined) this.player=value as Player;
          }
        })
        break;
      }
      case 'Cognome':{
        const dialogRef=this.dialog.open(ModificaDialogComponent,{
          data: { text: 'Qui puoi modificare il tuo cognome personale', type: type },
        })
        dialogRef.afterClosed().subscribe({
          next: value => {
            if (value!=null||value!=undefined) this.player=value as Player;
          }
        })
        break
      }
      case 'E-mail':{
        const dialogRef= this.dialog.open(ModificaDialogComponent,{
          data: { text: 'Ricorda: l\'e-mail non deve esser già utilizzata e deve esser del tipo example@example.com', type: type },
        })
        dialogRef.afterClosed().subscribe({
          next: value => {
            if (value!=null||value!=undefined) this.player=value as Player;
          }
        })
        break;
      }
      case 'Avatar':{
        const dialogRef = this.dialog.open(AvatarComponent, {
        });
        let avatarSelected:string="";
        dialogRef.afterClosed().subscribe((result:string) => {
          if (result!=null||result!=undefined) {
            avatarSelected = result.substring (result.lastIndexOf ("/") + 1);
            this.playerService.changeAvatar(avatarSelected).then(
                response =>{
                  this.toastr.success("Avatar cambiato con successo!")
                  if (response!=null||response!=undefined) this.player=response as Player;
                  dialogRef.close();},
                error =>{
                  this.toastr.error("Errore durante il cambio avatar!")
                })
          }

        });
        break;
      }
    }
  }
}
