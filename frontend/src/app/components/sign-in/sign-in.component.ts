import {Component , ViewChild} from '@angular/core';
import {NgForm} from "@angular/forms";
import {AuthService} from "../../auth/auth.service";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";
import {Player} from "../../data/player";
import {MatCheckbox} from "@angular/material/checkbox";

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrl: './sign-in.component.scss'
})
export class SignInComponent {
  hide: any=true;
  @ViewChild("checkbox") checkBox: MatCheckbox;

  constructor(private authService: AuthService,
              private toastr: ToastrService,
              private router: Router,
  ) {
  }

  loginWithEmailAndPassword(loginForm: NgForm) {
    if (loginForm.invalid) {
      this.toastr.error ("Completa il form!");
      return;
    }
    const user={
      username: loginForm.value.username,
      password: loginForm.value.password,
    }
    this.authService.signInWithEmailAndPassword(user,this.checkBox.checked).then(
        (response:Player|any) => {
          this.toastr.success("Login eseguito con successo!")
          this.router.navigate(['/homepage'],{state: {player: response}}).then();
        },error => {
          this.toastr.error("Errore durante il login: \n"+ error.message);
        }
    );
  }

  viewPassword(event: MouseEvent) {
    this.hide = !this.hide;
    event.stopPropagation();
  }

  forgotPassword ( username: string ) {
    if (!username) {
      this.toastr.error ("Devi inserire l'username");
      this.toastr.error (username);
      return;
    }
    this.authService.forgotPassword(username)
  }
}
