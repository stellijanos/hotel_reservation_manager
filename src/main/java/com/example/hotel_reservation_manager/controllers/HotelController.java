package com.example.hotel_reservation_manager.controllers;

import com.example.hotel_reservation_manager.models.Hotel;
import com.example.hotel_reservation_manager.services.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static java.util.stream.Collectors.toMap;

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


    @GetMapping("/ip")
    public String getIpAddress() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.ipify.org"))
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            return "";
        }
    }

    @GetMapping("/location")
    public HashMap<String, Object> getLocation() {

        String ipAddress = this.getIpAddress();


        if (ipAddress.isEmpty()) {
            return new HashMap<>();
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://ip-api.com/json/"+ipAddress))
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {


            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responsneBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responsneBody,HashMap.class);
        } catch (IOException | InterruptedException e) {
            return new HashMap<>();
        }

    }


    @GetMapping("/lat-lon")
    private HashMap<String, Double> getUserLocation() {

        HashMap<String, Object> locationInfo = this.getLocation();

        if (locationInfo.isEmpty()) {
            return new HashMap<>();
        }

        HashMap<String, Double> result = new HashMap<>();
        result.put("longitude", (Double) locationInfo.get("lat"));
        result.put("latitude", (Double) locationInfo.get("lon"));

        return result;
    }
}
