import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Hotel } from '../models/hotel';

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
