import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChessgameComponent } from './chessgame/chessgame.component';
import { ChessplayComponent } from './chessplay/chessplay.component';

@NgModule({
  declarations: [
    AppComponent,
    ChessgameComponent,
    ChessplayComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
