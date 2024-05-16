package com.example.hotel_reservation_manager.repositories;

import com.example.hotel_reservation_manager.models.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking, Long> {
}
