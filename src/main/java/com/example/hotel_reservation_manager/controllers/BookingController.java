package com.example.hotel_reservation_manager.controllers;

import com.example.hotel_reservation_manager.models.Booking;
import com.example.hotel_reservation_manager.models.Room;
import com.example.hotel_reservation_manager.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api")
@RestController
public class BookingController {

    @Autowired
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/hotel/{hotelId}/booking")
    public ResponseEntity<Booking> create(@PathVariable Long hotelId, @RequestBody Booking booking) {
        return ResponseEntity.ok(this.bookingService.create(hotelId, booking));
    }

    @GetMapping("/bookings")
    public ResponseEntity<Iterable<Booking>> getAll() {
        return ResponseEntity.ok(this.bookingService.getAll());
    }

    @GetMapping("/booking/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }


    @PatchMapping("/booking/{id}")
    public ResponseEntity<Booking> changeBookedRooms(@PathVariable Long id, @RequestBody List<Room> rooms) {
        return ResponseEntity.ok(this.bookingService.changeBookedRooms(id, rooms));
    }


    @DeleteMapping("/booking/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(this.bookingService.deleteById(id));
    }

}
