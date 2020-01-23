import { Component, OnInit, OnDestroy } from '@angular/core';
import { BorrowOrder } from 'src/app/model/borrowOrder';
import { BorrowOrderService } from 'src/app/service/borrow-order.service';
import { UserService } from 'src/app/service/user.service';
import { AuthenticationService } from 'src/app/service/auth/authentication.service';
import { User } from 'src/app/model/user';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ErrorInfoComponent } from '../dialogs/error-info/error-info.component';
import { MatDialog } from '@angular/material';
import { SubSink } from 'subsink';

@Component({
  selector: 'app-orders-view',
  templateUrl: './orders-view.component.html',
  styleUrls: ['./orders-view.component.scss']
})
export class OrdersViewComponent implements OnInit, OnDestroy {

  public borrowOrders: Array<BorrowOrder> = new Array<BorrowOrder>();
  private borrowOrder: BorrowOrder = new BorrowOrder();
  private user: User = new User();
  public isAdmin = false;
  public searchByUserId: FormGroup;
  public searchByBookId: FormGroup;
  public searchByOrderId: FormGroup;
  private subscriptions = new SubSink();

  constructor(private borrowOrderService: BorrowOrderService,
              private userService: UserService,
              private authenticationService: AuthenticationService,
              private dialog: MatDialog,
              private router: Router) { }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit() {
    this.searchByUserId = new FormGroup({
      userId: new FormControl(null, [Validators.required, Validators.minLength(1)])
    });

    this.searchByBookId = new FormGroup({
      bookId: new FormControl(null, [Validators.required, Validators.minLength(1)])
    });

    this.searchByOrderId = new FormGroup({
      orderId: new FormControl(null, [Validators.required, Validators.minLength(1)])
    });

    this.subscriptions.sink = this.userService.getUserByUsername(this.authenticationService.currentUserValue.username).subscribe(u => {
      this.user = u.content;

      if (this.user.role === 'ADMIN' || this.user.role === 'CREATOR') {
        this.isAdmin = true;
        this.getBorrowOrders();
      } else if (this.user.role === 'USER') {
        this.getBorrowOrdersByUserId(this.user.id);
      }
    });
  }

  public onSubmitByUserId() {
    this.subscriptions.sink = this.borrowOrderService.getBorrowOrdersByUserId(this.searchByUserId.value.userId).subscribe(orders => {
      this.borrowOrders = orders;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  public onSubmitByBookId() {
    this.subscriptions.sink = this.borrowOrderService.getBorrowOrdersByBookId(this.searchByBookId.value.bookId).subscribe(orders => {
      this.borrowOrders = orders;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  public onSubmitByOrderId() {
    this.subscriptions.sink = this.borrowOrderService.getBorrowOrderById(this.searchByOrderId.value.orderId).subscribe(order => {
      this.borrowOrder = order.content;
      this.router.navigate(['library/userpanel/ordersview/' + this.borrowOrder.id]);
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  private getBorrowOrders() {
    this.subscriptions.sink = this.borrowOrderService.getBorrowOrders().subscribe(bo => {
      this.borrowOrders = bo;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  private getBorrowOrdersByUserId(id: number) {
    this.subscriptions.sink = this.borrowOrderService.getBorrowOrdersByUserId(id).subscribe(bo => {
      this.borrowOrders = bo;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }
}
