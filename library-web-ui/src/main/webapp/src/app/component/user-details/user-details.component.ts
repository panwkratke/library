import { Component, OnInit, OnChanges, ChangeDetectorRef, ChangeDetectionStrategy, OnDestroy } from '@angular/core';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { AuthenticationService } from 'src/app/service/auth/authentication.service';
import { BorrowOrderService } from 'src/app/service/borrow-order.service';
import { BorrowOrder } from 'src/app/model/borrowOrder';
import { mergeMap } from 'rxjs/operators';
import { ErrorInfoComponent } from '../dialogs/error-info/error-info.component';
import { MatDialog } from '@angular/material';
import { SubSink } from 'subsink';
import { UserDeleteInfoComponent } from '../dialogs/user-delete-info/user-delete-info.component';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.scss'],
})

export class UserDetailsComponent implements OnInit, OnDestroy {

  public user: User = new User();
  private loggedUser: User = new User();
  private orders: Array<BorrowOrder> = new Array();
  public isCreator = false;
  public canDelete = false;
  public panelOpenState = false;
  private subscriptions = new SubSink();

  constructor(private userService: UserService,
              private authenticationService: AuthenticationService,
              private borrowOrderService: BorrowOrderService,
              private route: ActivatedRoute,
              private dialog: MatDialog,
              private router: Router,
              private cd: ChangeDetectorRef) { }

    ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit() {

    this.subscriptions.sink = this.userService.getUserByUsername(this.authenticationService.currentUserValue.username).subscribe(u => {
      this.loggedUser = u.content;
      if (this.loggedUser.role === 'CREATOR') {
        this.isCreator = true;
      } else {
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
      this.getUserById(param.get('id'));
      return this.borrowOrderService.getBorrowOrdersByUserId(param.get('id'));
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

  private getUserById(id: number) {
    this.subscriptions.sink = this.userService.getUserById(id).subscribe(u => {
      this.user = u.content;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  public deleteUser(id: number) {
    this.subscriptions.sink = this.userService.deleteUser(id).subscribe(u => {
      const dialogRef = this.dialog.open(UserDeleteInfoComponent, {data: {id: id}});
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

  public canDeleteUser(): boolean {
    return this.isCreator && this.canDelete ? true : false;
  }
}
