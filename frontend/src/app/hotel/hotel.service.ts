import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Hotel } from '../models/hotel';
import { Response } from '../models/response';

@Injectable({
  providedIn: 'root'
})
export class HotelService {

  private apiUrl: String = environment.apiUrl;

  
  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type' : 'application/json'
    })
  }

  constructor(private http: HttpClient) { }


  loadJson(): Observable<Response> {
    return this.http.post<Response>(`${this.apiUrl}/load-json`, {});
  }

  getAll():Observable<Hotel[]> {
    return this.http.get<Hotel[]>(`${this.apiUrl}/hotels`, this.httpOptions);
  }

  getById(id: Number): Observable<Hotel> {
    return this.http.get<Hotel>(`${this.apiUrl}/hotel/${id}`);
  }

  getFilteredByRadius(radius: Number): Observable<Hotel[]> {
    return this.http.get<Hotel[]>(`${this.apiUrl}/hotels/${radius}`)
  }

}
