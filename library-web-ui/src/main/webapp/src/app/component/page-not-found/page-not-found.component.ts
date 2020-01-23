import { Component, OnInit, OnDestroy } from '@angular/core';
import { SubSink } from 'subsink';
import { MatDialog } from '@angular/material';
import { Router } from '@angular/router';
import { PageNotFoundInfoComponent } from '../dialogs/page-not-found-info/page-not-found-info.component';
import { AuthenticationService } from 'src/app/service/auth/authentication.service';

@Component({
  selector: 'app-page-not-found',
  templateUrl: './page-not-found.component.html',
  styleUrls: ['./page-not-found.component.scss']
})
export class PageNotFoundComponent implements OnInit, OnDestroy {

  private subscriptions = new SubSink();

  constructor(private dialog: MatDialog,
              private router: Router,
              private authenticationService: AuthenticationService,) { }

  ngOnInit() {
    const dialogRef = this.dialog.open(PageNotFoundInfoComponent);
    this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
      if (this.authenticationService.currentUserValue) {
        this.router.navigate(['library/userpanel']);
      } else {
        this.router.navigate(['users/login']);
      }
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
