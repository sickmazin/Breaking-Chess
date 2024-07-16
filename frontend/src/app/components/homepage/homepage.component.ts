import {Component , OnInit} from '@angular/core';
import {Player} from "../../data/player";
import {Game} from "../../data/game";
import {Video} from "../../data/video";
import {Router} from "@angular/router";
import {AuthService} from "../../auth/auth.service";
import {ToastrService} from "ngx-toastr";
import {ChessplayService} from "../../service/chessplay.service";
import {liveGameDTO} from "../../data/liveGameDTO";

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
              private backend: ChessplayService,
              private toastrService: ToastrService
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
    this.backend.getGames().subscribe(
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
    console.log("Player: "+this.player)
  }

  showClassificaByModality(mode: string) {
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

  startGame(mode: string ) {
    this.backend.startGame(mode).toPromise().then(
       (response: liveGameDTO|undefined) => {
        this.router.navigate(['/matchmaking'],{state:{player: this.player,game:response}}).then(r=> {
          this.toastrService.success("Matchmaking iniziato!")
        })

      },
       err => {
        this.toastrService.error(err)
      }
    )
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
