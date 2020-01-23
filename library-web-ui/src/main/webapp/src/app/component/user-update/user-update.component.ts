import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { ErrorInfoComponent } from '../dialogs/error-info/error-info.component';
import { MatDialog } from '@angular/material';
import { SubSink } from 'subsink';
import { UpdateInfoComponent } from '../dialogs/update-info/update-info.component';

@Component({
  selector: 'app-user-update',
  templateUrl: './user-update.component.html',
  styleUrls: ['./user-update.component.scss']
})
export class UserUpdateComponent implements OnInit, OnDestroy {

  private user: User = new User();
  private updatedUser: User = new User();
  public updateForm: FormGroup;
  private subscriptions = new SubSink();

  constructor(private userService: UserService,
              private route: ActivatedRoute,
              private dialog: MatDialog,
              private router: Router) { }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit() {

    this.subscriptions.sink = this.route.paramMap.subscribe((param: Params) => {
      this.getUserById(param.get('id'));
    }, e => {
      const dialogRef = this.dialog.open(ErrorInfoComponent, {data: {error: e.error.apiErrorMsg}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['library/userpanel']);
      });
      console.log(e);
    });

    this.updateForm = new FormGroup({
      firstName: new FormControl(null, Validators.required),
      lastName: new FormControl(null, Validators.required),
      username: new FormControl(null, [Validators.required, Validators.minLength(4)]),
      password: new FormControl(null, [Validators.required, Validators.minLength(4)]),
    });
  }

  onSubmit() {
    this.updatedUser = this.updateForm.value;
    this.updatedUser.id = this.user.id;
    this.updatedUser.role = this.user.role;
    this.updateUser(this.updatedUser);
  }

  getUserById(id: number) {
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

  private updateUser(user: User) {
    this.subscriptions.sink = this.userService.updateUser(user).subscribe(u => {
      const dialogRef = this.dialog.open(UpdateInfoComponent);
      this.userService.onUpdateUserFinished(u.content);
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
