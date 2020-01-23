import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { User } from 'src/app/model/user';
import { Router } from '@angular/router';
import { UserService } from 'src/app/service/user.service';
import { DictionaryService } from 'src/app/service/dictionary.service';
import { MatDialog } from '@angular/material';
import { RegisterInfoComponent } from '../dialogs/register-info/register-info.component';
import { ErrorInfoComponent } from '../dialogs/error-info/error-info.component';
import { SubSink } from 'subsink';

@Component({
  selector: 'app-user-register',
  templateUrl: './user-register.component.html',
  styleUrls: ['./user-register.component.scss']
})
export class UserRegisterComponent implements OnInit, OnDestroy {

  public registerForm: FormGroup;
  private user: User = new User();
  public roles: Array<string> = new Array<string>();
  private subscriptions = new SubSink();

  constructor(private userService: UserService,
              private dictionaryService: DictionaryService,
              private router: Router,
              private dialog: MatDialog) { }

  ngOnInit() {
    this.registerForm = new FormGroup({
      firstName: new FormControl(null, [Validators.required, Validators.minLength(3)]),
      lastName: new FormControl(null, [Validators.required, Validators.minLength(3)]),
      username: new FormControl(null, [Validators.required, Validators.minLength(3)]),
      password: new FormControl(null, [Validators.required, Validators.minLength(3)]),
      role: new FormControl(null, Validators.required)
    });

    this.roles = this.dictionaryService.currentDictionaryValue.words;
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  onSubmit() {
    this.user = this.registerForm.value;
    this.registerUser(this.user);
  }

  private registerUser(user: User) {
    this.subscriptions.sink = this.userService.registerNewUser(user).subscribe(u => {
      this.userService.onRegisterUser(u.content);
      const dialogRef = this.dialog.open(RegisterInfoComponent, {data: {username: u.content.username}});
      this.subscriptions.sink = dialogRef.afterClosed().subscribe(r => {
        this.router.navigate(['users/login']);
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
