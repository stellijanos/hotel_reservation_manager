package com.example.hotel_reservation_manager.repositories;

import com.example.hotel_reservation_manager.models.Hotel;
import org.springframework.data.repository.CrudRepository;

public interface HotelRepository extends CrudRepository<Hotel, Long> {
}
