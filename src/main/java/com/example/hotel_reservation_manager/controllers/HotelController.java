package com.example.hotel_reservation_manager.controllers;

import com.example.hotel_reservation_manager.models.Hotel;
import com.example.hotel_reservation_manager.services.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

@RestController
@RequestMapping("/api")
public class HotelController {

    private final HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }


    @PostMapping("/load-json")
    public ResponseEntity<String> parseData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Iterable<Hotel> hotels = new HashSet<>(Arrays.asList(objectMapper.readValue(new File("hotels.json"), Hotel[].class)));
            this.hotelService.saveHotels(hotels);
            return ResponseEntity.ok("Hotels loaded successfully into the database");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Something went wrong" + e.toString());
        }
    }
}