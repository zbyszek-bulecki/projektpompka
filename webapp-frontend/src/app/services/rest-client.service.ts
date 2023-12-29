import { HttpClient, HttpHeaders, HttpParams, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, of } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RestClientService {

  constructor(private http: HttpClient) {    
  }

  public get(path: string, requestParams?: HttpParams | undefined):Observable<any>{
    return this.handleResponse(this.http.get(path, {observe: 'response', params: requestParams}));
  }

  public post(path: string, body: Object, requestParams?: HttpParams | undefined):Observable<any>{
    return this.handleResponse(this.http.post(path, body, {observe: 'response', params: requestParams}));
  }

  public put(path: string, body: Object, requestParams?: HttpParams | undefined):Observable<any>{
    return this.handleResponse(this.http.put(path, body, {observe: 'response', params: requestParams}));
  }

  public patch(path: string, body: Object, requestParams?: HttpParams | undefined):Observable<any>{
    return this.handleResponse(this.http.patch(path, body, {observe: 'response', params: requestParams}));
  }

  handleResponse(response: Observable<any>){
    return response.pipe(catchError(err => of(err)));
  }
}
