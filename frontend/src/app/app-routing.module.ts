import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {SignUpComponent} from "./components/sign-up/sign-up.component";
import {SignInComponent} from "./components/sign-in/sign-in.component";
import {MatchmakingComponent} from "./components/matchmaking/matchmaking.component";
import {HomepageComponent} from "./components/homepage/homepage.component";
import {NotfoundComponent} from "./components/notfound/notfound.component";
import {authGuard , authSignPage} from "./auth/auth.guard";
import {ChessplayComponent} from "./components/chessplay/chessplay.component";


const routes: Routes = [
  {path:'', redirectTo: 'game',pathMatch: "full" },
  {path:'signUp', component: SignUpComponent, canActivate: [authSignPage], pathMatch: 'full'},
  {path:'signIn', component: SignInComponent, canActivate: [authSignPage]},
  {path:'matchmaking', component: MatchmakingComponent, pathMatch: 'full'},
  {path:'homepage', component: HomepageComponent, canActivate: [authGuard]},
  {path:'game', component: ChessplayComponent },
  {path:'404', component:NotfoundComponent},
  {path:'**', redirectTo:'404'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
