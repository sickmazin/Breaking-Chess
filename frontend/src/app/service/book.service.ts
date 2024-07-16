import { Injectable } from '@angular/core';
import {HttpClient , HttpHeaders} from "@angular/common/http";
import {Book} from "../data/Book";
import {BOOK_URL , LIKE_URL} from "../support/constants";

@Injectable({
  providedIn: 'root'
})
export class BookService {

  constructor(private http:HttpClient) { }

  getBooks(){
    return this.http.get<Book[]>(`${BOOK_URL}`).toPromise();
  }

  addLike( book: Book ) {
    return this.http.put<Book>(LIKE_URL+book.id,book).toPromise();
  }

  deleteLike( book: Book ) {
    return this.http.delete<Book>(LIKE_URL+book.id).toPromise();
  }

  getLikes () {
    return this.http.get<Book[]>(`${BOOK_URL}/likes`).toPromise();
  }
}
