import {NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {ChessgameComponent, NgbdModalContent} from './components/chessgame/chessgame.component';
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
import {HttpClient, HttpClientModule} from "@angular/common/http";

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
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    MatFormField,
    MatIcon,
    MatCheckbox,
    MatIconButton,
    MatInput,
    MatLabel,
    MatButton,
    NgOptimizedImage,
    NgbdModalContent
  ],
  providers: [HttpClient],
  bootstrap: [AppComponent]
})
export class AppModule { }
