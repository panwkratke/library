import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) { }

  canActivate() {
    const currentUser = this.authenticationService.currentUserValue;
    if (currentUser.role === 'ADMIN' || currentUser.role === 'CREATOR') {
      return true;
    }
    this.router.navigate(['library/userpanel']);
    return false;
  }
}
