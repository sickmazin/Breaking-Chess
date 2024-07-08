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
  @ViewChild("checkbox") checkBox: MatCheckbox | undefined;

  constructor(private authService: AuthService,
              private toastr: ToastrService,
              private router: Router,
  ) {
  }

  loginWithEmailAndPassword(loginForm: NgForm) {
    const user={
      username: loginForm.value.username,
      password: loginForm.value.password,
    }
    this.authService.signInWithEmailAndPassword(user).then(
        (response:Player|any) => {
          console.log("Login eseguito con successo!")
          this.toastr.success("Login eseguito con successo!")
          this.router.navigate(['/homepage'],{state: {player: response}}).then();
        },error => {
          console.log("Errore durante il login: \n"+ error);
          this.toastr.error("Errore durante il login: \n"+ error);
          throw new Error( error);
        }
    );
  }

  viewPassword(event: MouseEvent) {
    this.hide = !this.hide;
    event.stopPropagation();
  }
}
