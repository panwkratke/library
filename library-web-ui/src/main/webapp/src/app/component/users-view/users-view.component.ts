import { Component, OnInit, OnDestroy } from '@angular/core';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ErrorInfoComponent } from '../dialogs/error-info/error-info.component';
import { MatDialog } from '@angular/material';
import { SubSink } from 'subsink';

@Component({
  selector: 'app-users-view',
  templateUrl: './users-view.component.html',
  styleUrls: ['./users-view.component.scss']
})
export class UsersViewComponent implements OnInit, OnDestroy {

  public users: Array<User> = new Array<User>();
  private user: User = new User();
  public searchByUserId: FormGroup;
  private subscriptions = new SubSink();

  constructor(private userService: UserService,
              private dialog: MatDialog,
              private router: Router) { }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit() {
    this.getUsers();

    this.searchByUserId = new FormGroup({
      userId: new FormControl(null, [Validators.required, Validators.minLength(1)])
    });
  }

  public onSubmitByUserId() {
    this.subscriptions.sink = this.userService.getUserById(this.searchByUserId.value.userId).subscribe(user => {
      this.user = user.content;
      this.router.navigate(['library/userpanel/usersview/' + this.user.id]);
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }

  private getUsers() {
    this.subscriptions.sink = this.userService.getUsers().subscribe(users => {
      this.users = users;
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });
  }
}
