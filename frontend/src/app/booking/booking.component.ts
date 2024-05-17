import { Component, OnInit } from '@angular/core';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { CommonModule } from '@angular/common';
import { Booking } from '../models/booking';
import { BookingService } from './booking.service';
import { Room } from '../models/room';
import { HotelService } from '../hotel/hotel.service';
import { Hotel } from '../models/hotel';

@Component({
  selector: 'app-booking',
  standalone: true,
  imports: [
    SidebarComponent,
    CommonModule
  ],
  templateUrl: './booking.component.html',
  styleUrl: './booking.component.css'
})
export class BookingComponent implements OnInit {

  showSpinner = true;
  bookings: Booking[] = [];
  hotels: Hotel[] = [];
  selectedBooking !: Booking;
  selectedHotel !: Hotel;
  errorMessage : string = '';
  successMessage : string = '';


  constructor(private bookingService: BookingService, private hotelService: HotelService) {}

  ngOnInit(): void {
    this.bookingService.getAll().subscribe((response: Booking[]) => {
      this.bookings = response;
      this.showSpinner = false;
    });
  }


  selectBookingAndHotel(booking: Booking) {
    this.hotelService.getById(booking.hotel.id).subscribe((response: Hotel) => {
      this.selectedHotel = response;
      this.selectedBooking = booking;
    });
  }

  getAvailableRooms(rooms: Room[]) {
    return rooms.filter(room => room.isAvailable);
  }


  selectRoom(room: Room) {
    room.isAvailable = false;
    this.selectedBooking.rooms.push(room);
    this.selectedHotel.rooms.forEach(r => {
      if (r.id === room.id) {
        r.isAvailable = false;
      }
    });
  }

  disSelectRoom(room: Room) {
    const index = this.selectedBooking.rooms.findIndex(r => r.roomNumber === room.roomNumber);
    if (index !== -1) {
      this.selectedBooking.rooms.splice(index, 1);
    } 
    this.selectedHotel.rooms.forEach(r => {
      if (r.id === room.id) {
        r.isAvailable = true;
      }
    });

  }


  getTotalPrice() {
    return this.selectedBooking.rooms.reduce((acc, room) => acc + Number(room.price), 0);
  }


  changeBookedRooms() {
    this.successMessage = this.errorMessage = '';
    this.bookingService.changeBookedRooms(this.selectedBooking.id, this.selectedBooking.rooms).subscribe((response: Booking) => {

      if (response !== null) {
        const index = this.bookings.findIndex(b => b.id === response.id);
        if (index !== -1) {
          this.bookings[index] = response;
          this.successMessage = "Booking updated successfully!";
        }
      } else {
        this.errorMessage = "Cannot update, there are less 2 hours before check-in!";
      }
    });
    console.log(this.selectedBooking);
  }

}
