import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { Observable, catchError, map } from 'rxjs';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
    Authorization: 'my-auth-token'
  })
};

@Injectable()
export class HeroesService {
  heroesUrl = 'api/heroes';  // URL to web api

  constructor(
    private http: HttpClient) {
  }

  /** GET heroes from the server */
  getHeroes(): Observable<String[]> {
    // @ts-ignore
    return this.http.get<String[]>(this.heroesUrl)
      .pipe(
        catchError(async err => console.log(err)) // TODO da aggiustare
      );
  }

  /* GET heroes whose name contains search term */
  searchHeroes(term: string): Observable<String[]> {
    term = term.trim();

    // Add safe, URL encoded search parameter if there is a search term
    const options = term ?
      { params: new HttpParams().set('name', term) } : {};

    // @ts-ignore
    return this.http.get<Hero[]>(this.heroesUrl, options)
      .pipe(
        catchError(async err => console.log(err)) // TODO da aggiustare
      );
  }

  // This JSONP example doesn't run. It is for the JSONP documentation only.
  /** Imaginary API in a different domain that supports JSONP. */
  heroesSearchUrl = 'https://heroes.com/search';

  /** Does whatever is necessary to convert the result from API to Heroes */
  jsonpResultToHeroes(result: any) { return result as String[]; }

  /* GET heroes (using JSONP) whose name contains search term */
  searchHeroesJsonp(term: string): Observable<String[]> {
    term = term.trim();

    const heroesUrl = `${this.heroesSearchUrl}?${term}`;
    // @ts-ignore
    return this.http.jsonp(heroesUrl, 'callback')
      .pipe(
        map(result => this.jsonpResultToHeroes(result)),
        catchError(async err => console.log(err)) // TODO da aggiustare
      );
  }

  //////// Save methods //////////

  /** POST: add a new hero to the database */
  addHero(hero: String): Observable<String> {
    // @ts-ignore
    return this.http.post<String>(this.heroesUrl, hero, httpOptions)
      .pipe(
        catchError(async err => console.log(err)) // TODO da aggiustare
      );
  }

  /** DELETE: delete the hero from the server */
  deleteHero(id: number): Observable<unknown> {
    const url = `${this.heroesUrl}/${id}`; // DELETE api/heroes/42
    return this.http.delete(url, httpOptions)
      .pipe(
        catchError(async err => console.log(err)) // TODO da aggiustare
      );
  }

  /** PUT: update the hero on the server. Returns the updated hero upon success. */
  updateHero(hero: String): Observable<String> {
    httpOptions.headers =
      httpOptions.headers.set('Authorization', 'my-new-auth-token');

    // @ts-ignore
    return this.http.put<String>(this.heroesUrl, hero, httpOptions)
      .pipe(
        catchError(async err => console.log(err)) // TODO da aggiustare
      );
  }
}
