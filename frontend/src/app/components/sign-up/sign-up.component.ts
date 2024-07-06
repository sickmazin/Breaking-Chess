import { Component } from '@angular/core';
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.scss'
})
export class SignUpComponent {
  hide: boolean=false;
  avatarSelected:string="";
  viewPassword(event: MouseEvent) {
    this.hide = !this.hide;
    event.stopPropagation();
  }

  registerWithEmailAndPassword(inputForm: NgForm) {

  }

  openAvatarDialog() {

  }

  createGame(bullet: string) {

  }
}
