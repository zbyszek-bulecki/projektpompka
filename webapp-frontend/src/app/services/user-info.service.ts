import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { RestClientService } from './rest-client.service';
import { Observable, catchError, map, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserInfoService{

  status: 'authenticated' | 'unauthenticated' | 'unknown' = 'unknown';

  userInfo: {
    username: string,
    role: string
  } | null = null;

  constructor(private restClient: RestClientService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.isUserLogged().pipe(map(isLogged => {
      console.log(isLogged);   
      if(!isLogged){
        this.router.navigate(["/"]);
      }  
      console.log(isLogged);    
      return isLogged;
    }));
  }

  isUserLogged(): Observable<boolean>{
    if(this.status === 'unknown'){
      return this.requestUserInfo();
    }
    return of(this.status === 'authenticated');
  }

  requestUserInfo(): Observable<boolean>{
    return this.restClient.get('/auth/info').pipe(
      map(data => {
        if(data.status==200){
          this.status = 'authenticated';
          if(data.role && data.username){
            this.userInfo = {
              username: data.username,
              role: data.role
            }
          }
        }
        else{
          this.status = 'unauthenticated';
        }
        return this.status === 'authenticated';
      }),
      catchError((): Observable<boolean> => {
        this.status = 'unauthenticated';
        return of(false);
      })
    )
  }

  public logout(){
    this.status = 'unknown';
    this.userInfo = null;
    this.restClient.get('/auth/logout').subscribe(response => {
      this.router.navigate(["/"]);
    });
  }

  public setAuthenticated(){
    this.status = "authenticated";
  }

  public setUnauthenticated(){
    this.status = "unauthenticated";
  }
}
