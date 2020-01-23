import { Component, OnInit } from '@angular/core';
import { DictionaryService } from './service/dictionary.service';
import { User } from './model/user';
import { AuthenticationService } from './service/auth/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'webapp';

  constructor(private dictionaryService: DictionaryService,
              private authenticationService: AuthenticationService,
              private router: Router) {}

  ngOnInit() {
    this.dictionaryService.initDictionaryByDictioanryName('RoleDictionary').subscribe(d => {
      console.log(d);
    }, e => {
      console.log(e);
    });
  }

  public logout() {
    this.authenticationService.logout(this.authenticationService.currentUserValue.username).subscribe(u => {
      this.router.navigate(['']);
    }, e => {
      console.log(e);
    });
  }

  public isLoggedIn() {
    return this.authenticationService.currentUserValue;
  }
}
