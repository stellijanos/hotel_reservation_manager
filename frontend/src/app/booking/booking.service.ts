import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Booking } from '../models/booking';
import { environment } from '../../environments/environment';
import { Room } from '../models/room';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  create(hotelId: Number, booking: Booking): Observable<Booking> {
    return this.http.post<Booking>(`${this.apiUrl}/hotel/${hotelId}/booking`, booking);
  }

  getAll(): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.apiUrl}/bookings`);
  }

  getById(id: Number): Observable<Booking> {
    return this.http.get<Booking>(`${this.apiUrl}/booking/${id}`);
  }

  changeBookedRooms(id: Number, rooms: Room[]): Observable<Booking> {
    return this.http.patch<Booking>(`${this.apiUrl}/booking/${id}`, rooms);
  }

  deleteById(id: Number):Observable<String> {
    return this.http.delete<String>(`${this.apiUrl}/booking/${id}`);
  }
  
}
