import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from 'src/app/model/user';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RestApiUrlConfig } from 'src/app/config/config';
import { ApiResponse } from 'src/app/model/apiResponse';
import { map } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private currentUserSubject: BehaviorSubject<User>;
  public $currentUser: Observable<User>;

  constructor(private http: HttpClient,
              private router: Router) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.$currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  public get token(): string {
    if (this.currentUserSubject.value) {
      return this.currentUserSubject.value.token;
    } else {
      return null;
    }
  }

  login(username: string, password: string) {
    const urlSearchParams = new URLSearchParams();
    urlSearchParams.append('username', username);
    urlSearchParams.append('password', password);
    const body = urlSearchParams.toString();

    const options = {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    };

    return this.http.post<any>(RestApiUrlConfig.REST_API_USER_LOGIN, body, options)
      .pipe(map((apiResp: ApiResponse<User>) => {
        this.saveNewToken(apiResp);
        return apiResp.content;
      }));
  }

  saveNewToken(apiResp: ApiResponse<User>) {
    localStorage.setItem('currentUser', JSON.stringify(apiResp.content));
    this.currentUserSubject.next(apiResp.content);
  }

  logout(username: string) {
    const urlSearchParams = new URLSearchParams();
    urlSearchParams.append('username', username);
    const body = urlSearchParams.toString();

    const options = {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    };

    return this.http.post<any>(RestApiUrlConfig.REST_API_USER_LOGOUT, body, options)
      .pipe(map((apiResp: ApiResponse<User>) => {
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
        this.router.navigate(['']);
      }));
  }
}
