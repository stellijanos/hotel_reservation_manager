import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Booking } from '../models/booking';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  create(booking: Booking): Observable<Booking> {
    return this.http.post<Booking>(`${this.apiUrl}/booking`, booking);
  }
}
