import { Component, OnInit } from '@angular/core';
import { Hotel } from '../models/hotel';
import { HotelService } from './hotel.service';

@Component({
  selector: 'app-hotel',
  standalone: true,
  imports: [],
  templateUrl: './hotel.component.html',
  styleUrl: './hotel.component.css'
})
export class HotelComponent implements OnInit {

  hotels: Hotel[] = [];

  constructor(private hotelService: HotelService) {
    
  }
  ngOnInit(): void {
    this.hotelService.getAll().subscribe((response: Hotel[]) => {
      this.hotels = response;

      console.log(this.hotels);
    });
  }


}
