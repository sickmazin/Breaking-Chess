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
    //TODO
  }

  viewPassword(event: MouseEvent) {
    this.hide = !this.hide;
    event.stopPropagation();
  }
}
