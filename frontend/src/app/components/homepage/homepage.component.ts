import {Component , OnInit} from '@angular/core';
import {Player} from "../../data/player";
import {Game} from "../../data/game";
import {Video} from "../../data/video";
import {Router} from "@angular/router";
import {AuthService} from "../../auth/auth.service";
import {GameService} from "../../service/game.service";

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent implements OnInit{
  leaderboard: Player[];
  isLightTheme: boolean;
  books: any;
  games: Game[];
  videos:Video[]; //this.youtubeService.getVideos()
  modality: number=1;
  player: Player;
  leardBoardModality: string="blitz";
  gettendInfo:boolean;
  constructor(private router: Router,
              private authService: AuthService,
              private gameService: GameService
              ) {
    if(this.router.getCurrentNavigation()!.extras.state?.['player']!=undefined ){
      this.player= this.router.getCurrentNavigation()!.extras.state?.['player']
      console.log("settato il player dal navigate")
      this.gettendInfo=true;
    }
  }

  ngOnInit(): void {
    if(!this.gettendInfo){
        this.authService.loginWithToken().then(
            (response:Player|any) => {
              this.player=response;
              this.gettendInfo=true
            },
            (error:any) => {
              throw new Error(error)
            }
        )
    }
    this.gameService.getGames().then(
        (response:Game[]|undefined) => {
          if(response!=undefined){
            this.games=response;
          }
          //TODO GESTICI QUANDO NON CI SONO GAME
        },
        error =>{
          throw  new Error(error)
        }
    )
    console.log(this.player)
  }

  showClassificaByModality( rapid: string ) {
    //TODO
  }

  openOptionsPage() {
    //TODO

  }

  logout() {
    //TODO

  }

  openFriendsPage() {
    //TODO
  }

  changeTheme() {
    this.isLightTheme=!this.isLightTheme;
  }

  openLink( link: string ) {
    window.open(link)
  }

  postGame( rapid: string ) {
    //TODO
  }

  changeModalityEloPoints() {
    if (this.modality == 3) this.modality=0
    this.modality+=1;
  }

  getPuntiEloByModality() {
    if (this.modality == 1) return this.player.bulletPoints;
    if (this.modality == 2) return this.player.blitzPoints;
    if (this.modality == 3) return this.player.rapidPoints;
    else return 0;
  }
}
