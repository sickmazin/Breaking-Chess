import {Component , OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {Player} from "../../app/data/player";
import {AuthService} from "../../app/auth/auth.service";

@Component({
  selector: 'app-optionspage',
  templateUrl: './optionspage.component.html',
  styleUrl: './optionspage.component.scss'
})
export class OptionspageComponent implements OnInit{
    player: Player;
    constructor ( protected router: Router,private authService: AuthService){
    }

    ngOnInit(): void {
        this.authService.loginWithToken().then (
            ( response: Player | any ) => {
                this.player = response;
            } ,
            ( error: any ) => {
                throw new Error (error)
            })
    }

    turnBackHome () {
        this.router.navigate (['homepage']).then ();
    }

    onOutletLoaded ( component: Component ) {
        // @ts-ignore inserisco questo perché so che tutti i component child hanno questo input
        component.player=this.player
    }
}
