import { Component, OnInit } from '@angular/core';
import { HotelService } from '../hotel/hotel.service';
import { Hotel } from '../models/hotel';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { CommonModule } from '@angular/common';
import { Room } from '../models/room';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    SidebarComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  hotels: Hotel[] = [];
  showSpinner = true;
  searchRadius!: number;

  constructor(private hotelService: HotelService) {
  }

  ngOnInit(): void {
    this.getAllHotels();
  }

  getNrOfAvailableRooms(rooms: Room[]) {
    return rooms.filter(room => room.isAvailable).length;
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



}
