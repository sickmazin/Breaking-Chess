import {CUSTOM_ELEMENTS_SCHEMA , inject , NgModule} from '@angular/core';
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
import {FormsModule , ReactiveFormsModule} from "@angular/forms";
import {AvatarComponent} from "./components/avatar/avatar.component";
import {SignInComponent} from "./components/sign-in/sign-in.component";
import {SignUpComponent} from "./components/sign-up/sign-up.component";
import {HomepageComponent} from "./components/homepage/homepage.component";
import {NotfoundComponent} from "./components/notfound/notfound.component";
import {MatchmakingComponent} from "./components/matchmaking/matchmaking.component";
import {LeaderboardElementComponent} from "./components/leaderboard-element/leaderboard-element.component";
import { GameDisplayComponent } from './components/game-display/game-display.component';
import {AuthInterceptor} from "./auth/interceptor.interceptor";
import {IonicModule} from "@ionic/angular";
import { AnimatedLikeComponent } from './components/animated-like/animated-like.component';
import {JwtModule} from "@auth0/angular-jwt";
import {AuthService} from "./auth/auth.service";


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
    AnimatedLikeComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule,
    CommonModule,
    BrowserAnimationsModule, // required animations module
    ToastrModule.forRoot(), // ToastrModule added
    MaterialModule,
    HttpClientModule,
    FormsModule,
    NgOptimizedImage,
    JwtModule.forRoot({
      config: {
        allowedDomains: ["example.com"],
        disallowedRoutes: ["http://example.com/examplebadroute/"],
      },
    }),
  ],
  providers: [
    provideAnimationsAsync(), { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule { }