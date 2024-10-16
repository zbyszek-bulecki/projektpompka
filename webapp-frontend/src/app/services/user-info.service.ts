import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { RestClientService } from './rest-client.service';
import { Observable, catchError, map, of } from 'rxjs';
import { HttpStatusCode } from '@angular/common/http';

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
      if(!isLogged){
        this.router.navigate(["/login"]);
      }
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
      map(response => {
        if(response.status==200){
          this.status = 'authenticated';
          this.setupUserData(response.body);
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

  public login(credentials: {username: string, password: string}){
    this.restClient.post('/auth/login', credentials).subscribe(response => {
      if(response.status === HttpStatusCode.Ok){
        this.setupUserData(response.body);
      }
    });
  }

  private setupUserData(data: any){
    this.status = 'authenticated';
    if(data.role && data.username){
      this.userInfo = {
        username: data.username,
        role: data.role
      }
      this.router.navigate(["/"]);
    }
  }

  public logout(){
    this.status = 'unknown';
    this.userInfo = null;
    this.restClient.get('/auth/logout').subscribe(response => {
      this.router.navigate(["/login"]);
    });
  }

  public setAuthenticated(){
    this.status = "authenticated";
  }

  public setUnauthenticated(){
    this.status = "unauthenticated";
  }
}
