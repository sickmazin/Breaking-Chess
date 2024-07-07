import {NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MDBBootstrapModule } from 'angular-bootstrap-md';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChessgameComponent } from './components/chessgame/chessgame.component';
import { ChessplayComponent } from './components/chessplay/chessplay.component';
import { LeaderboardElementComponent } from "./components/leaderboard-element/leaderboard-element.component";
import {AvatarComponent} from "./components/avatar/avatar.component";
import {HomepageComponent} from "./components/homepage/homepage.component";
import {SignInComponent} from "./components/sign-in/sign-in.component";
import {SignUpComponent} from "./components/sign-up/sign-up.component";
import {FormsModule} from "@angular/forms";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {NgOptimizedImage} from "@angular/common";

@NgModule({
  declarations: [
    AppComponent,
    ChessgameComponent,
    ChessplayComponent,
    LeaderboardElementComponent,
    AvatarComponent,
    HomepageComponent,
    SignInComponent,
    SignUpComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    MatFormField,
    MatIcon,
    MatCheckbox,
    MatIconButton,
    MatInput,
    MatLabel,
    MatButton,
    NgOptimizedImage
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
