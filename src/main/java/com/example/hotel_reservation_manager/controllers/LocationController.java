package com.example.hotel_reservation_manager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class LocationController {

    private static String getIpAddress() {
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


    private static HashMap<String, Object> getLocation() {

        String ipAddress = LocationController.getIpAddress();

        if (ipAddress.isEmpty()) {
            return new HashMap<>();
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://ip-api.com/json/" + ipAddress))
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responsneBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responsneBody, HashMap.class);

        } catch (IOException | InterruptedException e) {
            return new HashMap<>();
        }

    }


    public static HashMap<String, Double> getCoordinates() {

        HashMap<String, Object> locationInfo = LocationController.getLocation();

        if (locationInfo.isEmpty()) {
            return new HashMap<>();
        }

        HashMap<String, Double> result = new HashMap<>();
        result.put("lat", (Double) locationInfo.get("lat"));
        result.put("lon", (Double) locationInfo.get("lon"));

        return result;
    }


    // harversine formula
    public static Double convertToMeters(Double lat1, Double lon1, Double lat2, Double lon2) {
        final double R = 6378.137;
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d * 1000;
    }
}
