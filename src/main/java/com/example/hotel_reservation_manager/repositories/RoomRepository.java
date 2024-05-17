package com.example.hotel_reservation_manager.repositories;

import com.example.hotel_reservation_manager.models.Room;
import org.springframework.data.repository.CrudRepository;


public interface RoomRepository extends CrudRepository<Room, Long> {
}
