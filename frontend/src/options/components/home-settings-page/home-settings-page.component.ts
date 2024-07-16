import {Component , Input} from '@angular/core';
import {Player} from "../../../app/data/player";

@Component({
  selector: 'app-home-settings-page',
  templateUrl: './home-settings-page.component.html',
  styleUrl: './home-settings-page.component.scss'
})
export class HomeSettingsPageComponent {
  @Input() player: Player;
}
