import { Component, OnInit, OnDestroy } from '@angular/core';
import { BorrowOrder } from 'src/app/model/borrowOrder';
import { BorrowOrderService } from 'src/app/service/borrow-order.service';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { ErrorInfoComponent } from '../dialogs/error-info/error-info.component';
import { MatDialog } from '@angular/material';
import { SubSink } from 'subsink';
import { ReturnInfoComponent } from '../dialogs/return-info/return-info.component';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.scss']
})
export class OrderDetailsComponent implements OnInit, OnDestroy {

  public borrowOrder: BorrowOrder = new BorrowOrder();
  public borrowOrderLoaded = false;
  private subscriptions = new SubSink();

  constructor(private borrowOrderService: BorrowOrderService,
              private route: ActivatedRoute,
              private dialog: MatDialog,
              private router: Router) { }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit() {
    this.subscriptions.sink = this.route.paramMap.subscribe((param: Params) => {
      this.getBorrowOrderById(param.get('id'));
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  private getBorrowOrderById(id: number) {
    this.borrowOrderLoaded = false;
    this.subscriptions.sink = this.borrowOrderService.getBorrowOrderById(id).subscribe(bo => {
      this.borrowOrder = bo.content;
      this.borrowOrderLoaded = true;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  public returnBook(id: number) {
    this.subscriptions.sink = this.borrowOrderService.returnBorrowOrder(id).subscribe(bo => {
      const dialogRef = this.dialog.open(ReturnInfoComponent);
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
