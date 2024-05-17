package com.example.hotel_reservation_manager.controllers;

import com.example.hotel_reservation_manager.models.Booking;
import com.example.hotel_reservation_manager.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class BookingController {

    @Autowired
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/booking")
    public ResponseEntity<Booking> create(@RequestBody Booking booking) {
        return ResponseEntity.ok(this.bookingService.create(booking));
    }
}
