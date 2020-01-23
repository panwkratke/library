import { Injectable } from '@angular/core';
import { Observable, Subject, BehaviorSubject } from 'rxjs';
import { User } from '../model/user';
import { HttpClient } from '@angular/common/http';
import { ApiResponse } from '../model/apiResponse';
import { RestApiUrlConfig } from '../config/config';
import { AuthenticationService } from './auth/authentication.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  $registerUser: Observable<User>;
  $updateUserFinished: Observable<User>;
  private registerUser: Subject<User> = new Subject();
  private updateUserFinished: Subject<User> = new Subject();

  constructor(private http: HttpClient, private authenticationService: AuthenticationService) {
    this.$registerUser = this.registerUser.asObservable();
    this.$updateUserFinished = this.updateUserFinished.asObservable();
  }

  public onRegisterUser(user: User) {
    this.registerUser.next(user);
  }

  public onUpdateUserFinished(user: User) {
    this.updateUserFinished.next(user);
  }

  public registerNewUser(user: User): Observable<ApiResponse<User>> {
    return this.http.post<ApiResponse<User>>(RestApiUrlConfig.REST_API_USER_REGISTER, user);
  }

  public getUsers(): Observable<Array<User>> {
    return this.http.get<Array<User>>(RestApiUrlConfig.REST_API_USER_GET_LIST);
  }

  public getUserById(id: number): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(RestApiUrlConfig.REST_API_USER_GET_BY_ID + id);
  }

  public getUserByUsername(username: string): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(RestApiUrlConfig.REST_API_USER_GET_BY_USERNAME + username);
  }

  public deleteUser(id: number): Observable<ApiResponse<User>> {
    return this.http.delete<ApiResponse<User>>(RestApiUrlConfig.REST_API_USER_DELETE_BY_ID + id);
  }

  public updateUser(user: User): Observable<ApiResponse<User>> {
    return this.http.put<ApiResponse<User>>(RestApiUrlConfig.REST_API_USER_UPDATE_BY_ID, user).pipe(map(r => {
      this.authenticationService.saveNewToken(r);
      return r;
    }));
  }
}

