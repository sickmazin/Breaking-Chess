import {Component , signal} from '@angular/core';
export type MenuItem={
  icon: string;
  text: string;
  route?: string;
}
@Component({
  selector: 'app-custom-side-nav',
  templateUrl: './custom-side-nav.component.html',
  styleUrl: './custom-side-nav.component.scss'
})
export class CustomSideNavComponent {

  menuItems= signal<MenuItem[]>([
    {
      icon:'users',
      text:'Profilo',
      route: 'user'
    },
    {
      icon:'chess-board',
      text:'Statistiche',
      route: 'chessBoard'
    },
    {
      icon:'key',
      text:'Password',
      route: 'password'
    }
    ,{
      icon:'home',
      text:'Homepage',
      route: 'settingHomepage'
    },
  ]);
}
