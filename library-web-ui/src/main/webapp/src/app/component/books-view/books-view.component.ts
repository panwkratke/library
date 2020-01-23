import { Component, OnInit, OnDestroy } from '@angular/core';
import { Book } from 'src/app/model/book';
import { BookService } from 'src/app/service/book.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { UserService } from 'src/app/service/user.service';
import { AuthenticationService } from 'src/app/service/auth/authentication.service';
import { User } from 'src/app/model/user';
import { DictionaryService } from 'src/app/service/dictionary.service';
import { Router } from '@angular/router';
import { ErrorInfoComponent } from '../dialogs/error-info/error-info.component';
import { MatDialog } from '@angular/material';
import { SubSink } from 'subsink';

@Component({
  selector: 'app-books-view',
  templateUrl: './books-view.component.html',
  styleUrls: ['./books-view.component.scss']
})
export class BooksViewComponent implements OnInit, OnDestroy {

  private user: User = new User();
  public book: Book = new Book();
  public books: Array<Book> = new Array<Book>();
  public genres: Array<string> = new Array<string>();
  private title: string;
  private author: string;
  public searchByGenreForm: FormGroup;
  public searchByAuthorForm: FormGroup;
  public searchByTitleForm: FormGroup;
  public searchByTitleAndAuthorForm: FormGroup;
  public searchByBookId: FormGroup;
  public isAdmin = false;
  private subscriptions = new SubSink();

  constructor(private bookService: BookService,
              private userService: UserService,
              private authenticationService: AuthenticationService,
              private dictionaryService: DictionaryService,
              private dialog: MatDialog,
              private router: Router) { }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit() {
    this.getBooks();

    this.subscriptions.sink = this.dictionaryService.getDictionaryByDictioanryName('BookGenreDictionary').subscribe(d => {
      this.genres = d.content.words;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });

    this.searchByGenreForm = new FormGroup({
      genre: new FormControl(null, Validators.required)
    });

    this.searchByAuthorForm = new FormGroup({
      author: new FormControl(null, [Validators.required, Validators.minLength(4)])
    });

    this.searchByTitleForm = new FormGroup({
      title: new FormControl(null, [Validators.required, Validators.minLength(4)])
    });

    this.searchByBookId = new FormGroup({
      bookId: new FormControl(null, [Validators.required, Validators.minLength(1)])
    });

    this.searchByTitleAndAuthorForm = new FormGroup({
      author: new FormControl(null, [Validators.required, Validators.minLength(4)]),
      title: new FormControl(null, [Validators.required, Validators.minLength(4)])
    });

    this.subscriptions.sink = this.userService.getUserByUsername(this.authenticationService.currentUserValue.username).subscribe(u => {
      this.user = u.content;
      if (this.user.role === 'ADMIN' || this.user.role === 'CREATOR') {
        this.isAdmin = true;
      } else {
        this.isAdmin = false;
      }
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  public onSubmitByGenre() {
    this.subscriptions.sink = this.bookService.getBooksByGenre(this.searchByGenreForm.value.genre).subscribe(books => {
      this.books = books;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  public onSubmitByAuthor() {
    this.subscriptions.sink = this.bookService.getBooksByAuthor(this.searchByAuthorForm.value.author).subscribe(books => {
      this.books = books;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  public onSubmitByTitle() {
    this.subscriptions.sink = this.bookService.getBooksByTitle(this.searchByTitleForm.value.title).subscribe(books => {
      this.books = books;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  public onSubmitByBookId() {
    this.subscriptions.sink = this.bookService.getBookById(this.searchByBookId.value.bookId).subscribe(book => {
      this.book = book.content;
      this.router.navigate(['library/userpanel/booksview/' + this.book.id]);
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  public onSubmitByTitleAndAuthor() {
    this.title = this.searchByTitleAndAuthorForm.value.title;
    this.author = this.searchByTitleAndAuthorForm.value.author;
    this.subscriptions.sink = this.bookService.getBookByTitleAndAuthor(this.title, this.author).subscribe(book => {
      this.book = book.content;
      this.router.navigate(['library/userpanel/booksview/' + this.book.id]);
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  private getBooks() {
    this.subscriptions.sink = this.bookService.getBooks().subscribe(books => {
      this.books = books;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }
}
