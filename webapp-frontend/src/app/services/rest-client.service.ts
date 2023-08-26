import { HttpClient, HttpHeaders, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, of } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RestClientService {

  constructor(private http: HttpClient) {    
  }

  public get(path: string):Observable<any>{
    return this.handleResponse(this.http.get(path, {observe: 'response'}));
  }

  public post(path: string, body: Object):Observable<any>{
    return this.handleResponse(this.http.post(path, body, {observe: 'response'}));
  }

  public put(path: string, body: Object):Observable<any>{
    return this.handleResponse(this.http.put(path, body, {observe: 'response'}));
  }

  public patch(path: string, body: Object):Observable<any>{
    return this.handleResponse(this.http.patch(path, body, {observe: 'response'}));
  }

  handleResponse(response: Observable<any>){
    return response.pipe(catchError(err => of(err)));
  }
}
