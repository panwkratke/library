import { Component, OnInit, OnDestroy } from '@angular/core';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';
import { AuthenticationService } from 'src/app/service/auth/authentication.service';
import { Router } from '@angular/router';
import { ErrorInfoComponent } from '../dialogs/error-info/error-info.component';
import { MatDialog } from '@angular/material';
import { SubSink } from 'subsink';

@Component({
  selector: 'app-user-panel',
  templateUrl: './user-panel.component.html',
  styleUrls: ['./user-panel.component.scss']
})
export class UserPanelComponent implements OnInit, OnDestroy {

  public user: User = new User();
  public isAdmin = false;
  public panelOpenState = false;
  public expDate: string;
  private subscriptions = new SubSink();

  constructor(private userService: UserService,
              private authenticationService: AuthenticationService,
              private dialog: MatDialog,
              private router: Router) { }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit() {
    this.subscriptions.sink = this.userService.getUserByUsername(this.authenticationService.currentUserValue.username).subscribe(u => {
      this.user = u.content;
      if (this.user.role === 'ADMIN' || this.user.role === 'CREATOR') {
        this.isAdmin = true;
      } else if (this.user.role === 'USER') {
        this.isAdmin = false;
      }
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });

    this.subscriptions.sink = this.authenticationService.$currentUser.subscribe(u => {
      if (u) {
        const tokenParts: Array<string> = u.token.split('.');
        const token = atob(tokenParts[1]);
        const parsedToken = JSON.parse(token);
        const timeNow = new Date().getTime();
        this.expDate = new Date(timeNow + parsedToken.exp / 1000).toLocaleString('pl-PL', {timeZone: 'Europe/Warsaw'});
      }
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });

    this.subscriptions.sink = this.userService.$updateUserFinished.subscribe(u => {
      this.user = u;
      if (this.user.role === 'ADMIN' || this.user.role === 'CREATOR') {
        this.isAdmin = true;
      } else if (this.user.role === 'USER') {
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

  public home() {
    this.router.navigate(['library/userpanel']);
  }

  public logout(username: string) {
    this.subscriptions.sink = this.authenticationService.logout(username).subscribe(u => {
      console.log(u);
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }
}
