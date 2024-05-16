package com.example.hotel_reservation_manager.services;

import com.example.hotel_reservation_manager.models.Hotel;
import com.example.hotel_reservation_manager.models.Room;
import com.example.hotel_reservation_manager.repositories.HotelRepository;
import com.example.hotel_reservation_manager.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;


    @Autowired
    public HotelService(HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    public void saveHotels(Iterable<Hotel> hotels) {
        for (Hotel hotel: hotels) {
            for (Room room: hotel.getRooms()) {
                room.setHotel(hotel);
            }
        }
        hotelRepository.saveAll(hotels);
    }
}
