import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Book } from 'src/app/model/book';
import { BookService } from 'src/app/service/book.service';
import { Router } from '@angular/router';
import { DictionaryService } from 'src/app/service/dictionary.service';
import { ErrorInfoComponent } from '../dialogs/error-info/error-info.component';
import { MatDialog } from '@angular/material';
import { SubSink } from 'subsink';
import { BookAddInfoComponent } from '../dialogs/book-add-info/book-add-info.component';

@Component({
  selector: 'app-book-save',
  templateUrl: './book-save.component.html',
  styleUrls: ['./book-save.component.scss']
})
export class BookSaveComponent implements OnInit, OnDestroy {

  public saveBookForm: FormGroup;
  public genres: Array<string> = new Array<string>();
  private book: Book = new Book();
  private subscriptions = new SubSink();

  constructor(private bookService: BookService,
              private dictionaryService: DictionaryService,
              private dialog: MatDialog,
              private router: Router) { }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit() {

    this.saveBookForm = new FormGroup({
      author: new FormControl(null, [Validators.required, Validators.minLength(4)]),
      title: new FormControl(null, [Validators.required, Validators.minLength(4)]),
      quantity: new FormControl(null, Validators.required),
      genre: new FormControl(null, Validators.required)
    });

    this.subscriptions.sink = this.dictionaryService.getDictionaryByDictioanryName('BookGenreDictionary').subscribe(d => {
      this.genres = d.content.words;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  onSubmit() {
    this.book = this.saveBookForm.value;
    this.book.pages = this.setRandomBookPages(30, 500);
    this.saveBook(this.book);
  }

  private saveBook(book: Book) {
    this.subscriptions.sink = this.bookService.saveNewBook(book).subscribe(b => {
      this.bookService.onSaveBook(b.content);
      const dialogRef = this.dialog.open(BookAddInfoComponent, {data: {title: b.content.title}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  private setRandomBookPages(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }
}
