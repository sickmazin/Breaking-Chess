import { Component } from '@angular/core';
import {NgForm} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {AvatarComponent} from "../avatar/avatar.component";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.scss'
})
export class SignUpComponent {

  hide: boolean=false;
  avatarSelected:string="";
  constructor(
      private dialog : MatDialog, ) {
  }
  viewPassword(event: MouseEvent) {
    this.hide = !this.hide;
    event.stopPropagation();
  }

  registerWithEmailAndPassword(inputForm: NgForm) {
    //TODO
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
