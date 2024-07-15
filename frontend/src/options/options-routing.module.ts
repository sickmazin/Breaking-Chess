import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {OptionspageComponent} from "./optionspage/optionspage.component";
import {UserPageComponent} from "./components/user-page/user-page.component";
import {HomeSettingsPageComponent} from "./components/home-settings-page/home-settings-page.component";
import {ChessBoardPageComponent} from "./components/chess-board-page/chess-board-page.component";
import {PasswordPageComponent} from "./components/password-page/password-page.component";

const routes: Routes = [
  {path:'options', component: OptionspageComponent, children :[
      {path:'user', component: UserPageComponent},
      {path:'settingHomepage', component: HomeSettingsPageComponent },
      {path:'chessBoard', component: ChessBoardPageComponent  },
      {path:'password', component: PasswordPageComponent},
    ]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OptionsRoutingModule { }
