import { HttpClient, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RestClientService {

  constructor(private http: HttpClient) {    
  }

  public get(url: string):Observable<any>{
    return this.handleResponse(this.http.get(url, {observe: 'response'}));
  }

  public post(url: string, body: Object):Observable<any>{
    return this.handleResponse(this.http.post(url, body, {observe: 'response'}));
  }

  public put(url: string, body: Object):Observable<any>{
    return this.handleResponse(this.http.put(url, body, {observe: 'response'}));
  }

  public patch(url: string, body: Object):Observable<any>{
    return this.handleResponse(this.http.patch(url, body, {observe: 'response'}));
  }

  handleResponse(response: Observable<any>){
    return response.pipe(catchError(err => of(err)));
  }
}
