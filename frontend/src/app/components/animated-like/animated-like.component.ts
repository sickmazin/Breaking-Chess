import {Component , EventEmitter , Input , OnInit , Output} from '@angular/core';
import {animate , state , style , transition , trigger} from "@angular/animations";
import {Book} from "../../data/Book";
import {BookService} from "../../service/book.service";

@Component({
  selector: 'app-animated-like',
  templateUrl: './animated-like.component.html',
  styleUrl: './animated-like.component.scss',
  animations: [
    trigger('heart', [
      state('unliked', style({
        color: '#fff',
        opacity: '0.7',
        transform: 'scale(1)'
      })),
      state('liked', style({
        color: '#e74c3c',
        opacity: '1',
        transform: 'scale(1)'
      })),
      transition('unliked <=> liked', animate('100ms ease-out'))
    ])
  ]
})
export class AnimatedLikeComponent implements OnInit{
  public likeState: string = 'unliked';
  public iconName: string = 'like';
  @Input() libro: Book;
  @Input() isThereLike: boolean;

  @Output() dataEvent = new EventEmitter<Book>();

  sendDataToParent(
      data: Book
  ) {
    this.dataEvent.emit(data);
  }

  constructor(private bookService: BookService) { }

  ngOnInit(): void {
    if (this.isThereLike){
      this.likeState = 'liked';
      this.iconName = 'likefull';
      return;
    }
  }


  toggleLikeState ( book: Book ) {
    if (this.likeState == 'unliked') {
      this.likeState = 'liked';
      this.iconName = 'likefull';
      this.bookService.addLike(book).then (
          response => {
            if (response != undefined) {
              this.sendDataToParent(response)
            }},
          error => {
            throw new Error(error)
      });
    } else {
      this.likeState = 'unliked';
      this.iconName = 'like';
      this.bookService.deleteLike(book).then (
          response => {
            if (response != undefined) {
              this.sendDataToParent(response)
            }},
          error => {
            throw new Error(error)
          });
    }
  }
}
