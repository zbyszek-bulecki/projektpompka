import { Injectable } from '@angular/core';
import { UserInfoService } from './user-info.service';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AnonymousUserService {

  constructor(private userInfoService: UserInfoService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.userInfoService.isUserLogged().pipe(map(isLogged => {
      if(isLogged){
        this.router.navigate(["/devices"]);
      }      
      return !isLogged;
    }));
  }
}
