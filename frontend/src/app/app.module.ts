import {CUSTOM_ELEMENTS_SCHEMA , NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {RouterModule} from "@angular/router";
import {CommonModule , NgOptimizedImage} from "@angular/common";
import {MaterialModule} from "./MaterialModule/material.module";
import {HTTP_INTERCEPTORS , HttpClientModule , provideHttpClient} from "@angular/common/http";
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
import {AuthInterceptor} from "./auth/interceptor.interceptor";

@NgModule({
  declarations: [
    AppComponent,
    SignInComponent,
    SignUpComponent,
    NotfoundComponent,
    AvatarComponent,
    HomepageComponent,
    MatchmakingComponent,
    GameDisplayComponent,
    LeaderboardElementComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule,
    CommonModule,
    MaterialModule,
    HttpClientModule,
    BrowserAnimationsModule, // required animations module
    ToastrModule.forRoot(),
    FormsModule,
    NgOptimizedImage,
  ],
  providers: [
    provideAnimationsAsync(), { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },//  {provide:APP_INITIALIZER, deps:[KeycloakService],useFactory:initializer,multi:true}
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule { }