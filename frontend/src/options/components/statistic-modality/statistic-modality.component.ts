import {Component , Input , OnInit} from '@angular/core';
import {Stat} from "../../../app/data/Stat";
import {PlayerService} from "../../../app/service/player.service";

@Component({
  selector: 'app-statistic-modality',
  templateUrl: './statistic-modality.component.html',
  styleUrl: './statistic-modality.component.scss'
})
export class StatisticModalityComponent implements OnInit{
  constructor (private playerService: PlayerService) {
  }
  ngOnInit(): void {
    this.statistic (this.modality)
  }
  @Input() player_username:string;
  @Input() modality: string;
  stat: Stat;
  statistic ( modality: string ) {
    this.playerService.getStatFor(modality,this.player_username).subscribe(
        response =>{
          this.stat=response as Stat;
        },
        error =>{
          this.stat={
            modality:modality,
            win:0,
            lose:0,
            draw:0
          }
          throw new Error(error)

        }
    );
  }
}
