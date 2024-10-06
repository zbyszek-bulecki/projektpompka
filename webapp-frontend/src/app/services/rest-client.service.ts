import { HttpClient, HttpHeaders, HttpParams, HttpResponse, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RestClientService {

  constructor(private http: HttpClient) {    
  }

  public get<T>(path: string, requestParams?: HttpParams | undefined):Observable<HttpResponse<T>>{
    return this.handleResponse(this.http.get(path, {observe: 'response', params: requestParams}));
  }

  public post<T>(path: string, body: Object, requestParams?: HttpParams | undefined):Observable<HttpResponse<T>>{
    return this.handleResponse(this.http.post(path, body, {observe: 'response', params: requestParams}));
  }

  public put<T>(path: string, body: Object, requestParams?: HttpParams | undefined):Observable<HttpResponse<T>>{
    return this.handleResponse(this.http.put(path, body, {observe: 'response', params: requestParams}));
  }

  public patch<T>(path: string, body: Object, requestParams?: HttpParams | undefined):Observable<HttpResponse<T>>{
    return this.handleResponse(this.http.patch(path, body, {observe: 'response', params: requestParams}));
  }

  handleResponse<T>(response: Observable<HttpResponse<T>>){
    return response.pipe(catchError(err => of(err)));
  }
}
