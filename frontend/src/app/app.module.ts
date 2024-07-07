import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {RouterModule} from "@angular/router";
import {CommonModule , NgOptimizedImage} from "@angular/common";
import {MaterialModule} from "./MaterialModule/material.module";
import {provideHttpClient} from "@angular/common/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ToastrModule} from "ngx-toastr";
import {FormsModule} from "@angular/forms";
import {AvatarComponent} from "./components/avatar/avatar.component";
import {SignInComponent} from "./components/sign-in/sign-in.component";
import {SignUpComponent} from "./components/sign-up/sign-up.component";
import {HomepageComponent} from "./components/homepage/homepage.component";
import {NotfoundComponent} from "./components/notfound/notfound.component";
import {MatchmakingComponent} from "./components/matchmaking/matchmaking.component";
import {LeaderboardElementComponent} from "./components/leaderboard-element/leaderboard-element.component";
import { GameDisplayComponent } from './components/game-display/game-display.component';

@NgModule({
  declarations: [
    AppComponent,
    AvatarComponent,
    SignInComponent,
    SignUpComponent,
    HomepageComponent,
    NotfoundComponent,
    HomepageComponent,
    MatchmakingComponent,
    LeaderboardElementComponent,
    GameDisplayComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule,
    CommonModule,
    MaterialModule,
    BrowserAnimationsModule, // required animations module
    ToastrModule.forRoot(),
    FormsModule,
    NgOptimizedImage,
  ],
  providers: [
    provideAnimationsAsync(),
    provideHttpClient()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
