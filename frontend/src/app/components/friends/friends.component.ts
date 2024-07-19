import {Component , OnInit} from '@angular/core';
import {FriendService} from "../../service/friend.service";
import {Player} from "../../data/player";
import {FRIEND_URL} from "../../support/constants";
import {BehaviorSubject} from "rxjs";
import {MatTooltip} from "@angular/material/tooltip";
import {MatDialog} from "@angular/material/dialog";
import {PopupStatisticComponent} from "../popup-statistic/popup-statistic.component";
import {Router} from "@angular/router";

@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrl: './friends.component.scss'
})
export class FriendsComponent implements OnInit{
  friendsSub: BehaviorSubject<any>;
  requestsSub: BehaviorSubject<any>;
  friends: Player[]
  requests: Player[]

  constructor (private friendService: FriendService, private router: Router, private dialog: MatDialog) {}

  ngOnInit() {
    this.getFriends()
    this.getPendingRequests()
    this.friendsSub = new BehaviorSubject<Player[]>(this.friends)
    this.requestsSub = new BehaviorSubject<Player[]>(this.requests)
  }

  getFriends () {
     this.friendService.getFriends().subscribe(
      res => this.friends = res
    )
  }

  getPendingRequests() {
     this.friendService.getPendingRequests().subscribe(
        res => this.requests = res
    )
  }

  delete ( player: string ) {
    this.friendService.delete(player).subscribe({
      next: value =>{
        this.getFriends ()
        this.getPendingRequests ()
      },
      error: err => {
        console.log(err)
      }
    })
  }

  acceptRequest ( player: string ) {
    this.friendService.acceptRequest(player).subscribe({
      next:res => {
        this.getFriends ()
        this.getPendingRequests ()
      },
      error: err => {
        console.log(err)
      }
    })
  }

  statsFriend ( username: string ) {
    const dialogRef= this.dialog.open(PopupStatisticComponent,{data:{username: username}})
  }

  challenge ( username: string ) {
    //TODO
  }
  turnBackHome () {
    this.router.navigate (['homepage']).then ();
  }
}
