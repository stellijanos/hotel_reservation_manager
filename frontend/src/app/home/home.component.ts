import { Component, OnInit } from '@angular/core';
import { HotelService } from '../hotel/hotel.service';
import { Hotel } from '../models/hotel';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    SidebarComponent,
    CommonModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  hotels: Hotel[] = [];

  showSpinner = true;

  constructor(private hotelService: HotelService) {
  }

  ngOnInit(): void {
    this.hotelService.getAll().subscribe((response: Hotel[]) => {
      this.hotels = response;

      console.log(this.hotels);
    });
  }

}
