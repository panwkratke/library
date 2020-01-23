import { Component, OnInit, OnDestroy } from '@angular/core';
import { Book } from 'src/app/model/book';
import { CreateBorrowOrderDto } from 'src/app/model/createBorrowOrderDto';
import { User } from 'src/app/model/user';
import { BookService } from 'src/app/service/book.service';
import { UserService } from 'src/app/service/user.service';
import { BorrowOrderService } from 'src/app/service/borrow-order.service';
import { AuthenticationService } from 'src/app/service/auth/authentication.service';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { MatDialog } from '@angular/material';
import { ErrorInfoComponent } from '../dialogs/error-info/error-info.component';
import { BorrowOrder } from 'src/app/model/borrowOrder';
import { mergeMap } from 'rxjs/operators';
import { SubSink } from 'subsink';
import { BorrowInfoComponent } from '../dialogs/borrow-info/borrow-info.component';
import { BookDeleteInfoComponent } from '../dialogs/book-delete-info/book-delete-info.component';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.scss']
})
export class BookDetailsComponent implements OnInit, OnDestroy {

  private createBorrowOrderDto: CreateBorrowOrderDto = new CreateBorrowOrderDto();
  public book: Book = new Book();
  public user: User = new User();
  private orders: Array<BorrowOrder> = new Array();
  public isAdmin = false;
  public isCreator = false;
  public canDelete = false;
  private subscriptions = new SubSink();

  constructor(private bookService: BookService,
              private userService: UserService,
              private borrowOrderService: BorrowOrderService,
              private authenticationService: AuthenticationService,
              private route: ActivatedRoute,
              private router: Router,
              private dialog: MatDialog) {

  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit() {

    this.subscriptions.sink = this.userService.getUserByUsername(this.authenticationService.currentUserValue.username).subscribe(u => {
      this.user = u.content;
      if (this.user.role === 'ADMIN') {
        this.isAdmin = true;
      } else if (this.user.role === 'CREATOR') {
        this.isCreator = true;
        this.isAdmin = true;
      } else {
        this.isAdmin = false;
        this.isCreator = false;
      }
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });

    this.subscriptions.sink = this.route.paramMap.pipe(mergeMap((param: Params) => {
      this.getBook(param.get('id'));
      return this.borrowOrderService.getBorrowOrdersByBookId(param.get('id'));
    })).subscribe(bo => {
      this.orders = bo;
      this.canDelete = this.orders.length === 0;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  public borrowBook(bookId: number) {
    this.createBorrowOrderDto.userId = this.user.id;
    this.createBorrowOrderDto.bookId = bookId;
    this.subscriptions.sink = this.borrowOrderService.createBorrowOrder(this.createBorrowOrderDto).subscribe(bo => {
      this.borrowOrderService.onCreateBorrowOrder(bo.content);
      const dialogRef = this.dialog.open(BorrowInfoComponent, {data: {title: bo.content.book.title}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel/booksview']);
      });
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  public deleteBook(id: number) {
    this.subscriptions.sink = this.bookService.deleteBookById(id).subscribe(u => {
      const dialogRef = this.dialog.open(BookDeleteInfoComponent, {data: {id: id}});
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

  public getBook(id: number) {
    this.subscriptions.sink = this.bookService.getBookById(id).subscribe(b => {
      this.book = b.content;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  public updateBook(id: number) {
    this.bookService.onUpdateBookId(id);
    this.router.navigate(['/library/userpanel/booksview/updateBook/upd']);
  }

  public canDeleteBook(): boolean {
    return this.isCreator && this.canDelete ? true : false;
  }
}
