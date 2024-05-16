import { Component, OnInit } from '@angular/core';
import { HotelService } from '../hotel/hotel.service';
import { Hotel } from '../models/hotel';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { CommonModule } from '@angular/common';
import { Room } from '../models/room';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Booking } from '../models/booking';
import { BookingService } from '../booking/booking.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SidebarComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  hotels: Hotel[] = [];
  showSpinner = true;
  searchRadius!: number;
  selectedHotel!: Hotel;
  createBookingForm!: FormGroup;
  selectedRooms: Room[] = [];
  totalPrice: Number = 0;
  successMessage: String = '';
  errorMessage: String = '';

  constructor(
    private hotelService: HotelService, 
    private bookingService: BookingService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.getAllHotels();

    this.createBookingForm = this.formBuilder.group({
      firstname: ['', [Validators.required]],
      lastname: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required]],
      booking_date: ['', [Validators.required]]
    });
  }


  getAvailableRooms(rooms: Room[]) {
    return rooms.filter(room => room.isAvailable);
  }

  getNrOfAvailableRooms(rooms: Room[]) {
    return this.getAvailableRooms(rooms).length;
  }

  getAllHotels() {
    this.hotelService.getAll().subscribe((response: Hotel[]) => {
      this.hotels = response;
      this.showSpinner = false;
    });
  }

  getFilteredHotels(radius: number) {
    this.hotelService.getFilteredByRadius(radius).subscribe((response: Hotel[]) => {
      this.hotels = response;
      this.showSpinner = false;
    });
  }


  search() {

    this.showSpinner = true;
    let radius = this.searchRadius || 0;

    if (radius !== 0) {
     this.getFilteredHotels(radius);
    } else {
      this.getAllHotels();
    }
    
  }

  toggleRoom(room: Room) {

    const index = this.selectedRooms.findIndex(r => r.roomNumber === room.roomNumber);

    if (index !== -1) {
      this.selectedRooms.splice(index, 1);
      this.totalPrice = Number(this.totalPrice) - Number(room.price);
     
    } else {
      this.selectedRooms.push(room);
      this.totalPrice = Number(this.totalPrice) + Number(room.price);
    }
    console.log(this.selectedRooms);
  }

  createBooking() {

    this.successMessage = this.errorMessage = '';

    if (this.createBookingForm.valid) {
      let booking: Booking = this.createBookingForm.value;
      booking.rooms = this.selectedRooms;
      booking.price = this.totalPrice;

      console.log(booking);

      this.bookingService.create(booking).subscribe((response: Booking) => {
        if (booking.id) {
          this.successMessage = "Booking was successful!";
        } else {
          this.errorMessage = "Something went wrong";
        }
      });
    }

  }

}
