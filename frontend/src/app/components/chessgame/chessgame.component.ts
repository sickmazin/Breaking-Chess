import {Component, inject, Input, OnDestroy, OnInit} from '@angular/core';
import { Chess } from 'chess.js'
import $, {error} from 'jquery';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NgOptimizedImage} from "@angular/common";
import {ChessplayService} from "../../service/chessplay.service";
import {liveGameDTO} from "../../data/liveGameDTO";
import {Player} from "../../data/player";


declare var ChessBoard: any;

const whiteSquareHigh = '#a9a9a9'
const blackSquareHigh = '#696969'
const removeHighlightedSquares = ()=> {
  $('#board1 .square-55d63').css('background', '')
}
const highlightSquare = (square: any) => {
  const $square = $('#board1 .square-' + square);

  let background = whiteSquareHigh;
  if ($square.hasClass('black-3c85d')) {
    background = blackSquareHigh
  }

  $square.css('background', background)
}
const onMouseoutSquare = (square: any, piece: any) => {
  removeHighlightedSquares()
}


@Component({
  selector: 'app-chessgame',
  templateUrl: './chessgame.component.html',
  styleUrl: './chessgame.component.scss'
})

export class ChessgameComponent implements OnInit {
  board: any;
  modalService = inject(NgbModal)

  config: any;

  @Input() game!: Chess;
  @Input() liveGameDTO: liveGameDTO;
  @Input() player: Player;

  constructor(private backend: ChessplayService) {
  }


  ngOnInit(): void {
    // @ts-ignore
    let onDragStart = (source: any, piece: any) => {
      // do not pick up pieces if the game is over
      if (this.game.isGameOver()) return false

      // or if it's not that side's turn
      if (this.player.username !== this.liveGameDTO.turn) {
        console.log("NOT MY TURN")
        return false
      }
    }
    // @ts-ignore
    let onDrop = async (source: any, target: any) => {
      removeHighlightedSquares()

      let found = false
      // see if the move is legal
      const moves = this.game.moves({verbose: true});
      for (let i=0; i<moves.length; i++) {
        if (moves[i].from === source && moves[i].to === target) {
          found = true;

          // NOTE: check for promotion
          let promotionP: string ='';
          if ('promotion' in moves[i]) {
            const modalRef = this.modalService.open(NgbdModalContent, { size : <any>'sm'})
            modalRef.componentInstance.mySide = this.game.turn()
            promotionP = await modalRef.result;
          }

          let castle: string = ""
          let pieceObj = this.game.get(source)
          let piece: string = pieceObj.type
          if (pieceObj.color === "w") {
            piece = piece.toUpperCase()
            promotionP = promotionP.toUpperCase()
          }
          if (moves[i].flags === "k" || moves[i].flags === "q") castle = "c"

          console.log("backend move is "+piece+source+target+promotionP+castle)

          this.backend.makeMove(piece+source+target+promotionP+castle).subscribe(
            res => this.game = new Chess(res.fen.at(res.fen.length-1)),
            err => console.log(err)
          )

          if (promotionP!=='') onSnapEnd()
          break;
        }
      }
      if (!found) {
        // illegal move
        console.log("illegal")
        return "snapback"
      }
    }
    let onMouseoverSquare = (square: any, piece: any) => {
      // get list of possible moves for this square
      try {
        var moves = this.game.moves({ //TODO get moves from square
          square: square,
          verbose: true
        })
      } catch (e) {
        return;
      }

      // exit if there are no moves available for this square
      if (moves.length === 0) return

      // highlight the square they moused over
      highlightSquare(square)

      // highlight the possible squares for this piece
      for (let i = 0; i < moves.length; i++) {
        highlightSquare(moves[i].to)
      }
    }
    let onSnapEnd = () => {
      this.board.position(this.game.fen()) //TODO get game fen
    }

    this.config = {
      draggable: true,
      position: 'start',
      onDragStart: onDragStart,
      onDrop: onDrop,
      onMouseoutSquare: onMouseoutSquare,
      onMouseoverSquare: onMouseoverSquare,
      onSnapEnd: onSnapEnd
    }
    this.startGame(this.liveGameDTO.whitePlayer)
  }

  startGame(white: string) {
    this.board = ChessBoard('board1', this.config)
    if (white!==this.player.username) this.board.orientation('black')
  }
}

@Component({
  selector: 'ngbd-modal-content',
  standalone: true,
  imports: [
    NgOptimizedImage
  ],
  template: `
    <div class="modal-body" style="align-self: center" >
      <ul id="promotion" class="promotion" style="justify-content: space-between; margin-right: 8px !important; margin-top: 5px !important">
        <img (click)="setPromotion('q')" ngSrc="img/chesspieces/wikipedia/{{mySide}}Q.png" width="50" height="50"
             alt="queen">
        <img (click)="setPromotion('r')" ngSrc="img/chesspieces/wikipedia/{{mySide}}R.png" width="50" height="50"
             alt="rook">
        <img (click)="setPromotion('b')" ngSrc="img/chesspieces/wikipedia/{{mySide}}B.png" width="50" height="50"
             alt="bishop">
        <img (click)="setPromotion('n')" ngSrc="img/chesspieces/wikipedia/{{mySide}}N.png" width="50" height="50"
             alt="knight">
      </ul>
    </div>
  `
})
export class NgbdModalContent {
  mySide: string | undefined;
  activeModal = inject(NgbActiveModal);


  setPromotion(piece: string) {
    this.activeModal.close(piece)
  }
}
