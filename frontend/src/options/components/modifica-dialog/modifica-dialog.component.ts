import {Component , Inject} from '@angular/core';
import {MAT_DIALOG_DATA , MatDialogRef} from "@angular/material/dialog";
import {ToastrService} from "ngx-toastr";
import {PlayerService} from "../../../app/service/player.service";

@Component({
  selector: 'app-modifica-dialog',
  templateUrl: './modifica-dialog.component.html',
  styleUrl: './modifica-dialog.component.scss'
})
export class ModificaDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: {text: string, type:string},
              private toastr: ToastrService,
              private playerService: PlayerService,
              public dialogRef: MatDialogRef<ModificaDialogComponent>,
              ) { }

  checkSave ( inputValue: string, type: string ) {
    if (!inputValue || inputValue.trim().length === 0) {
      this.toastr.error('Inserisci qualcosa nell\'input field prima di salvare!');
    } else {
      switch (type) {
        case 'Nome': {
          this.playerService.changeFirstName(inputValue).then(
              response =>{
                this.toastr.success("Nome cambiato con successo!")
                this.dialogRef.close(response);
              },
              error =>{
                this.toastr.error("Errore durante il cambio nome!")
              }
          )
          break;
        }
        case 'Nickname':{
          this.playerService.changeNickname(inputValue).then(
              response =>{
                this.toastr.success("Nickname cambiato con successo!")
                this.dialogRef.close(response);
              },
              error =>{
                this.toastr.error("Errore durante il cambio Nickname!")
              }
          )
          break;
        }
        case 'Cognome':{
          this.playerService.changeLastName(inputValue).then(
              response =>{
                this.toastr.success("Cognome cambiato con successo!")
                this.dialogRef.close(response);
              },
              error =>{
                this.toastr.error("Errore durante il cambio cognome!")
              }
          )
          break
        }
        case 'E-mail':{
          this.playerService.changeEmail(inputValue).then(
              response =>{
                this.toastr.success("E-mail cambiata con successo!")
                this.dialogRef.close(response);
              },
              error =>{
                this.toastr.error("Errore durante il cambio E-mail!")
              }
          )
          break;
        }
      }
    }

  }

  onNoClick () {
      this.dialogRef.close();
  }
}
