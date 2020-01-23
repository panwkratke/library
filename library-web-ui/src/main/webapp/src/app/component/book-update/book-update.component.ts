import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Book } from 'src/app/model/book';
import { BookService } from 'src/app/service/book.service';
import { Router } from '@angular/router';
import { DictionaryService } from 'src/app/service/dictionary.service';
import { ErrorInfoComponent } from '../dialogs/error-info/error-info.component';
import { MatDialog } from '@angular/material';
import { SubSink } from 'subsink';
import { UpdateInfoComponent } from '../dialogs/update-info/update-info.component';

@Component({
  selector: 'app-book-update',
  templateUrl: './book-update.component.html',
  styleUrls: ['./book-update.component.scss']
})
export class BookUpdateComponent implements OnInit, OnDestroy {

  public updateBookForm: FormGroup;
  private bookId: number;
  private updatedBook: Book = new Book();
  public genres: Array<string> = new Array<string>();
  private subscriptions = new SubSink();

  constructor(private bookService: BookService,
              private dictionaryService: DictionaryService,
              private dialog: MatDialog,
              private router: Router) { }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit() {

    this.subscriptions.sink = this.bookService.$updateBookId.subscribe(id => {
      this.bookId = id;
    });

    this.updateBookForm = new FormGroup({
      id: new FormControl(null),
      author: new FormControl(null, [Validators.required, Validators.minLength(4)]),
      title: new FormControl(null, [Validators.required, Validators.minLength(4)]),
      genre: new FormControl(null, Validators.required),
      pages: new FormControl(null, Validators.required),
      quantity: new FormControl(null, Validators.required)
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
    this.updatedBook = this.updateBookForm.value;
    this.updatedBook.id = this.bookId;
    this.updateBookById(this.updatedBook);
  }

  private updateBookById(book: Book) {
    this.subscriptions.sink = this.bookService.updateBook(book).subscribe(b => {
      const dialogRef = this.dialog.open(UpdateInfoComponent);
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
}
