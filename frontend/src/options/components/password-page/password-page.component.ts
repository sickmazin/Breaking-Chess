import {Component , Input} from '@angular/core';
import {Player} from "../../../app/data/player";

@Component({
  selector: 'app-password-page',
  templateUrl: './password-page.component.html',
  styleUrl: './password-page.component.scss'
})
export class PasswordPageComponent {
  @Input() player: Player;

}
