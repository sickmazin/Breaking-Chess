import { Component, HostListener, Input, OnInit, ViewChild} from '@angular/core';
import { Chess } from 'chess.js'
import $ from 'jquery';
import { BehaviorSubject} from "rxjs";


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

  constructor() {
  }

  config: any;
  promotion: boolean = false;
  promotionPiece: BehaviorSubject<String> = new BehaviorSubject<String>('');
  promotionPiece$ = this.promotionPiece.asObservable()

  @Input() game!: Chess;
  @ViewChild('promotionDiv') div: HTMLDivElement | undefined;
  /*@Input()*/ mySide: String = 'w';

  @HostListener('document:onmouseup', ['$event'])
  handleMouseUpWhenPromotion(event: MouseEvent) {
    if (!this.promotion) return
    if (this.div != undefined) {
      this.div.style.left = ''+event.x;
      this.div.style.top = ''+event.y;
    }
    else console.log("DIV INDEFINITO")
  }

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
    let onDrop = (source: any, target: any) => {
      removeHighlightedSquares()

      let found = false
      // see if the move is legal
      const moves = this.game.moves({verbose: true});
      for (let i=0; i<moves.length; i++) {
          if (moves[i].from === source && moves[i].to === target) {
            found = true;
            // NOTE: check for promotion
            if ('promotion' in moves[i]) {
              this.promotion = true;
              this.promotionPiece$.subscribe(
                value => {
                  this.game.move({
                    from: source,
                    to: target,
                    promotion: String(value)
                  })
                  onSnapEnd()
                  this.promotion = false;
                }
              )
            }
            this.game.move({
              from: source,
              to: target
            })
            this.mySide = this.game.turn()
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
    console.log("fatto")
  }

  startGame() {
    this.board = ChessBoard('board1', this.config)
  }

  setPromotion(piece: String) {
    this.promotionPiece.next(piece)
  }
}
