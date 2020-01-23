import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { User } from 'src/app/model/user';
import { AuthenticationService } from 'src/app/service/auth/authentication.service';
import { Router } from '@angular/router';
import { UserService } from 'src/app/service/user.service';
import { first } from 'rxjs/operators';
import { ErrorInfoComponent } from '../dialogs/error-info/error-info.component';
import { MatDialog } from '@angular/material';
import { SubSink } from 'subsink';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.scss']
})
export class UserLoginComponent implements OnInit, OnDestroy {

  public loginForm: FormGroup;
  private user: User = new User();
  private subscriptions = new SubSink();

  constructor(private authenticationService: AuthenticationService,
              private router: Router,
              private dialog: MatDialog,
              private userService: UserService) { }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit() {
    this.loginForm = new FormGroup({
      username: new FormControl(null, [Validators.required, Validators.minLength(4)]),
      password: new FormControl(null, [Validators.required, Validators.minLength(4)])
    });
  }

  onSubmit() {
    this.subscriptions.sink = this.authenticationService.login(this.loginForm.value.username, this.loginForm.value.password)
      .pipe(first())
      .subscribe(
        data => {
          this.userService.getUserByUsername(this.loginForm.value.username).subscribe(u => {
            this.user = u.content;
            this.router.navigate(['library/userpanel']);
          });
        },
        error => {
          const dialogRef = this.dialog.open(ErrorInfoComponent, { data: { error: error.error.apiErrorMsg } });
          this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
            this.router.navigate(['library/userpanel']);
          });
          console.log(error);
        }
      );
  }
}
