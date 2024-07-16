import { NgModule } from '@angular/core';
import {CommonModule , NgOptimizedImage} from '@angular/common';
import { OptionsRoutingModule } from './options-routing.module';
import { ChessBoardPageComponent } from './components/chess-board-page/chess-board-page.component';
import { PasswordPageComponent } from './components/password-page/password-page.component';
import { HomeSettingsPageComponent } from './components/home-settings-page/home-settings-page.component';
import { UserPageComponent } from './components/user-page/user-page.component';
import { StatisticModalityComponent } from './components/statistic-modality/statistic-modality.component';
import {MatIcon} from "@angular/material/icon";
import {MatFormField , MatLabel , MatOption , MatSelect} from "@angular/material/select";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import { ModificaDialogComponent } from './components/modifica-dialog/modifica-dialog.component';
import {MatDialogActions , MatDialogClose , MatDialogContent , MatDialogTitle} from "@angular/material/dialog";


@NgModule({
    declarations: [
        ChessBoardPageComponent ,
        PasswordPageComponent ,
        HomeSettingsPageComponent ,
        UserPageComponent ,
        StatisticModalityComponent,
        ModificaDialogComponent
    ] ,
    exports: [
        UserPageComponent
    ] ,
    imports: [
        CommonModule ,
        OptionsRoutingModule ,
        NgOptimizedImage ,
        MatIcon ,
        MatLabel ,
        MatSelect ,
        MatFormField ,
        MatOption ,
        MatInput ,
        MatButton ,
        MatDialogActions ,
        MatDialogContent ,
        MatDialogClose ,
        MatDialogTitle
    ]
})
export class OptionsModule { }
