import {Component, inject, Input, OnInit} from '@angular/core';
import { Chess } from 'chess.js'
import $ from 'jquery';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NgOptimizedImage} from "@angular/common";


declare var ChessBoard: any;

@Component({
  selector: 'app-chessgame',
  templateUrl: './chessgame.component.html',
  styleUrl: './chessgame.component.scss'
})

export class ChessgameComponent implements OnInit {
  board: any;
  whiteSquareHigh = '#a9a9a9'
  blackSquareHigh = '#696969'
  modalService = inject(NgbModal)

  config: any;

  @Input() game!: Chess;
  /*@Input()*/ mySide: string = 'w';


  ngOnInit(): void {
    let removeHighlightedSquares = ()=> {
      $('#board1 .square-55d63').css('background', '')
    }
    let highlightSquare = (square: any) => {
      const $square = $('#board1 .square-' + square);

      let background = this.whiteSquareHigh;
      if ($square.hasClass('black-3c85d')) {
        background = this.blackSquareHigh
      }

      $square.css('background', background)
    }
    // @ts-ignore
    let onDragStart = (source: any, piece: any) => {
      // do not pick up pieces if the game is over
      if (this.game.isGameOver()) return false

      // or if it's not that side's turn
      if ((this.game.turn() === 'w' && piece.search(/^b/) !== -1) ||
        (this.game.turn() === 'b' && piece.search(/^w/) !== -1)) {
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
              modalRef.componentInstance.mySide = this.mySide
              promotionP = await modalRef.result;
            }
            this.game.move({
              from: source,
              to: target,
              promotion: promotionP
            })
            this.mySide = this.game.turn()
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
    let onMouseoutSquare = (square: any, piece: any) => {
      removeHighlightedSquares()
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

    this.startGame()
  }

  startGame() {
    this.board = ChessBoard('board1', this.config)
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
