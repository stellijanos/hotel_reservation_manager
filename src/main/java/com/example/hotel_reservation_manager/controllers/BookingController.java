package com.example.hotel_reservation_manager.controllers;

import com.example.hotel_reservation_manager.models.Booking;
import com.example.hotel_reservation_manager.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.OptionalLong;

@RequestMapping("/api")
@RestController
public class BookingController {

    @Autowired
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/hotel/{hotelId}/booking")
    public ResponseEntity<?> create(@PathVariable Long hotelId, @RequestBody Booking booking) {
        return ResponseEntity.ok(this.bookingService.create(hotelId, booking));
    }
}
