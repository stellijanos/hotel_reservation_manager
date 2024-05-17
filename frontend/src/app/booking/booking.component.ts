import { Component, OnInit } from '@angular/core';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { CommonModule } from '@angular/common';
import { Booking } from '../models/booking';
import { BookingService } from './booking.service';
import { Room } from '../models/room';
import { HotelService } from '../hotel/hotel.service';
import { Hotel } from '../models/hotel';
import { Response } from '../models/response';

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
  successMessage : string = '';
  errorMessage : string = '';
  modalSuccessMessage: string = '';
  modalErrorMessage: string = '';


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

    this.modalErrorMessage = this.modalSuccessMessage = '';

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

  getBookings() {
    return this.bookings;
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
          this.modalSuccessMessage = "Booking updated successfully!";
        }
      } else {
        this.modalErrorMessage = "Cannot update, there are less 2 hours before check-in!";
      }
    });

  }

  cancelBooking(id: Number) {
    this.showSpinner = true;
    
    this.bookingService.deleteById(id).subscribe((response: Response) => {
  
      if (response.response == "ok") {
        this.bookings = this.bookings.filter(booking => booking.id !== id);
        this.successMessage = "Booking cancelled successfully!";
      } else {
        this.errorMessage = "Cannot cancel, there are less 2 hours before check-in!";
      }
      this.showSpinner = false;
    })
  }

}
