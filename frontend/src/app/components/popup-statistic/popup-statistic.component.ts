import {Component , Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Player} from "../../data/player";

@Component({
  selector: 'app-popup-statistic',
  templateUrl: './popup-statistic.component.html',
  styleUrl: './popup-statistic.component.scss'
})
export class PopupStatisticComponent {
  constructor (@Inject(MAT_DIALOG_DATA) public data: {username: string}){}
}
