package com.example.hotel_reservation_manager.controllers;

import com.example.hotel_reservation_manager.models.Hotel;
import com.example.hotel_reservation_manager.services.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.*;


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



    @GetMapping("/hotels")
    public ResponseEntity<Iterable<Hotel>> getAll() {
        return ResponseEntity.ok(this.hotelService.getAll());
    }


    @GetMapping("/hotels/{radius}")
    public ResponseEntity<Iterable<Hotel>> getAllFiltered(@PathVariable Double radius) {
        Iterable<Hotel> hotels = this.hotelService.getAll();
        List<Hotel> result = new ArrayList<>();

        HashMap<String, Double> coordinates = LocationController.getCoordinates();
        Double lat = coordinates.get("lat");
        Double lon = coordinates.get("lon");

        for (Hotel hotel: hotels) {
            Double distance = LocationController.convertToMeters(lat, lon, hotel.getLatitude(), hotel.getLongitude());
            // radius * 1000 -> convertire din km in m
            if (distance <= radius * 1000) {
                result.add(hotel);
            }
        }
        return ResponseEntity.ok(result);
    }
}
