import { Injectable } from '@angular/core';
import { Observable, Subject, BehaviorSubject } from 'rxjs';
import { Book } from '../model/book';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ApiResponse } from '../model/apiResponse';
import { RestApiUrlConfig } from '../config/config';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  $saveBook: Observable<Book>;
  private saveBook: Subject<Book> = new Subject();
  $updateBookId: Observable<number>;
  private updateBookId: BehaviorSubject<number>;

  constructor(private http: HttpClient) {
    this.$saveBook = this.saveBook.asObservable();
    this.updateBookId = new BehaviorSubject<number>(null);
    this.$updateBookId = this.updateBookId.asObservable();
  }

  public onSaveBook(book: Book) {
    this.saveBook.next(book);
  }

  public onUpdateBookId(id: number) {
    this.updateBookId.next(id);
  }

  public saveNewBook(book: Book): Observable<ApiResponse<Book>> {
    return this.http.post<ApiResponse<Book>>(RestApiUrlConfig.REST_API_BOOK_SAVE, book);
  }

  public getBooks(): Observable<Array<Book>> {
    return this.http.get<Array<Book>>(RestApiUrlConfig.REST_API_BOOK_GET_LIST);
  }

  public getBooksByAuthor(author: string): Observable<Array<Book>> {
    return this.http.get<Array<Book>>(RestApiUrlConfig.REST_API_BOOK_GET_LIST_BY_AUTHOR + author);
  }

  public getBooksByGenre(genre: string): Observable<Array<Book>> {
    return this.http.get<Array<Book>>(RestApiUrlConfig.REST_API_BOOK_GET_LIST_BY_GENRE + genre);
  }

  public getBooksByTitle(title: string): Observable<Array<Book>> {
    return this.http.get<Array<Book>>(RestApiUrlConfig.REST_API_BOOK_GET_LIST_BY_TITLE + title);
  }

  public getBookById(id: number): Observable<ApiResponse<Book>> {
    return this.http.get<ApiResponse<Book>>(RestApiUrlConfig.REST_API_BOOK_GET_BY_ID + id);
  }

  public getBookByTitleAndAuthor(title: string, author: string): Observable<ApiResponse<Book>> {
    let params = new HttpParams();
    params = params.append('title', title);
    params = params.append('author', author);

    return this.http.get<ApiResponse<Book>>(RestApiUrlConfig.REST_API_BOOK_GET_BY_TITLE_AND_AUTHOR, {params});
  }

  public deleteBookById(id: number): Observable<ApiResponse<Book>> {
    return this.http.delete<ApiResponse<Book>>(RestApiUrlConfig.REST_API_BOOK_DELETE_BY_ID + id);
  }

  public updateBook(book: Book): Observable<ApiResponse<Book>> {
    return this.http.put<ApiResponse<Book>>(RestApiUrlConfig.REST_API_BOOK_UPDATE_BY_ID, book).pipe(map(r => {
      return r;
    }));
  }

}
