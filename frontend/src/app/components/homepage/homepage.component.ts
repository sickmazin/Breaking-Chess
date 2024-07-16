import {Component , OnInit} from '@angular/core';
import {Player} from "../../data/player";
import {Game} from "../../data/game";
import {Video} from "../../data/video";
import {Router} from "@angular/router";
import {AuthService} from "../../auth/auth.service";
import {GameService} from "../../service/game.service";
import {ToastrService} from "ngx-toastr";
import {LeaderboardService} from "../../service/leaderboard.service";
import {BookService} from "../../service/book.service";
import {Book} from "../../data/Book";
import {YoutubeService} from "../../service/youtube.service";
import {liveGame} from "../../data/liveGame";

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent implements OnInit{
    loadingBooks: boolean;
    loadingLeaderboard: boolean;
    loadingGames: boolean;
    typeElo: string="bullet";
    leaderboard: Player[];
    isLightTheme: boolean;
    books: Book[];
    likes: Book[];
    games: Game[];
    videos: Video[];
    modality: number = 1;
    player: Player;
    leardBoardModality: string = "blitz";
    gettendInfo: boolean;

    constructor( private router: Router ,
                 private authService: AuthService ,
                 private gameService: GameService ,
                 private toastrService: ToastrService ,
                 private leaderboardService: LeaderboardService ,
                 private bookService: BookService ,
                 private youtubeService: YoutubeService ,
    ) {
        if (this.router.getCurrentNavigation ()!.extras.state?.[ 'player' ] != undefined) {
            this.player = this.router.getCurrentNavigation ()!.extras.state?.[ 'player' ]
            console.log ("settato il player dal navigate")
            //refreshing del token
            //this.authService.loginWithToken().then()
            this.gettendInfo = true;
        }
    }

    ngOnInit(): void {
        //GETTING INFO FROM DB
        if (!this.gettendInfo) {
            this.authService.loginWithToken ().then (
                ( response: Player | any ) => {
                    this.player = response;
                    this.gettendInfo = true
                } ,
                ( error: any ) => {
                    throw new Error (error)
                })
        }

        //GETTING GAMES FROM DB
        this.showGames ()
        //GETTING CLASSIFICA FROM DB
        this.showClassificaByModality ("BLITZ")
        //GETTING BOOk FROM DB
        this.getBooks()
        //VIDEO FROM API
        this.videos = this.youtubeService.getVideos() // TODO DOVREBBE ESSER FIXATO IL FATTO CHE SE VAI IN MATCH E TORNI INDIETRO I VIDEO SI RADDOPPIANO

    }

    showClassificaByModality( modality: string ) {
        this.loadingLeaderboard=true
        this.leardBoardModality = modality;
        this.leaderboard = [];
        this.leaderboardService.getLeaderboardByModality (modality).then (
            ( response: Player[] | undefined ) => {
                if (response != undefined) {
                    this.loadingLeaderboard=false;
                    this.leaderboard = response;
                } else {
                    this.leaderboard = [];
                }
            })
    }

    showGames() {
        this.loadingGames=true
        this.gameService.getGames().then (
            ( response: Game[] | undefined ) => {
                if (response != undefined) {
                    this.games = response;
                    if(this.games.length!=0) this.loadingGames=false
                }
            } ,
            error => {
                throw new Error (error)
            }
        )
    }

    getBooks() {
        this.loadingBooks=true
        this.bookService.getBooks().then (
            ( response: Book[] | undefined ) => {
                if (response != undefined) {
                    this.loadingBooks=false;
                    this.books = response;
                } else {
                    this.books = [];
                }
            }
        )
        this.bookService.getLikes().then (
            ( response: Book[] | undefined ) => {
                if (response != undefined) {
                    this.likes = response as Book[];
                } else {
                    this.likes = [];
                }
            }
        )
    }

    openOptionsPage() {
        this.router.navigate (["options"]).then ()
    }

    logout() {
        this.authService.logout()
    }

    openFriendsPage() {
        //TODO
    }

    changeTheme() {
        this.isLightTheme = !this.isLightTheme;
    }

    //COMPLETED
    openLink( link: string ) {
        window.open (link)
    }


  startGame(mode: string ) {
    this.backend.startGame(mode).toPromise().then(
       (response: liveGameDTO|undefined) => {
        this.router.navigate(['/matchmaking'],{state:{player: this.player,game:response}}).then(r=> {
          this.toastrService.success("Matchmaking iniziato!")
            },
       err => {
        this.toastrService.error(err)
      }
    )}
  }
    changeModalityEloPoints() {
        if (this.modality==3) this.modality=0;
        this.modality += 1;
        switch (this.modality) {
            case 1:
                this.typeElo = "bullet";
                break;
            case 2:
                this.typeElo = "blitz";
                break;
            case 3:
                this.typeElo = "rapid";
                break;
        }

    }

    getPuntiEloByModality() {
        switch (this.modality){
            case 1: return this.player.bulletPoints;
            case 2: return this.player.blitzPoints;
            case 3:  return this.player.rapidPoints;
            default: return 0;
        }
    }

    trackById( index: number , item: any ): number {
        if (item as Player) return item.username;
        else return item.id;
    }

    isThereLike ( libro: Book ) {
        //return this.likes.includes(libro) NON FUNZIONA
        for (const book of this.likes) {
            if (book.id == libro.id) return true
        }
        return false;
    }

    handleDataFromChild ( $event: Book ) {
        for (let i = 0; i < this.books.length; i++) {
            if(this.books[i].id==$event.id){
                this.books[i]=$event;
            }
        }
    }
}
