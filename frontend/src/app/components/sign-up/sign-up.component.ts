import { Component } from '@angular/core';
import {NgForm} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {AvatarComponent} from "../avatar/avatar.component";
import {AuthService} from "../../auth/auth.service";
import {Player} from "../../data/player";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.scss'
})
export class SignUpComponent {

  hide: boolean=false;
  avatarSelected:string="";
  constructor(
      private dialog : MatDialog,
      private authService: AuthService,
      private toastr: ToastrService,
      private router: Router,
      ) {
  }
  viewPassword(event: MouseEvent) {
    this.hide = !this.hide;
    event.stopPropagation();
  }

  registerWithEmailAndPassword(inputForm: NgForm) {
    const player={
      email: inputForm.value.email,
      password: inputForm.value.password,
      firstName: inputForm.value.firstName,
      lastName: inputForm.value.lastName,
      username: inputForm.value.username,
      avatar: this.avatarSelected,
    }
    this.authService.signUpWithEmailAndPassword(player).then(
        (resp:Player|undefined) => {
          this.toastr.success("Registrazione eseguita con successo!");
          this.router.navigate(['../homepage'] , {state: {player: resp}}).then();
        },
        error =>{
          console.log("ERRORE DURANTE LA REGISTRAZIONE: "+error);
          this.toastr.error("Errore durante la registrazione: \n"+error.message);
        }
    )
  }

  openAvatarDialog() {
    const dialogRef = this.dialog.open(AvatarComponent, {
    });
    dialogRef.afterClosed().subscribe((result:any) => {
      console.log('The dialog was closed');
      this.avatarSelected=result;
    });

  }

}
