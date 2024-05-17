package com.example.hotel_reservation_manager.services;

import com.example.hotel_reservation_manager.models.Booking;
import com.example.hotel_reservation_manager.models.Hotel;
import com.example.hotel_reservation_manager.models.Room;
import com.example.hotel_reservation_manager.repositories.BookingRepository;
import com.example.hotel_reservation_manager.repositories.HotelRepository;
import com.example.hotel_reservation_manager.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }


    public Booking create(Long hotelId, Booking booking) {

        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        if (hotel == null) {
            return null;
        }
        booking.setHotel(hotel);

        List<Long> roomNumbers = booking.getRooms().stream().map(Room::getId).collect(Collectors.toList());
        List<Room> rooms = ((List<Room>) roomRepository.findAllById(roomNumbers))
                .stream()
                .filter(room -> room.getHotel().getId().equals(hotelId) && room.getIsAvailable())
                .peek(room -> room.setIsAvailable(false))
                .toList();
        booking.setRooms(rooms);

        return this.bookingRepository.save(booking);
    }


    public Iterable<Booking> getAll() {
        return this.bookingRepository.findAll();
    }

    public Booking getById(Long id) {
        return this.bookingRepository.findById(id).orElse(null);
    }

    public Booking changeBookedRooms(Long id, List<Room> rooms) {
        return this.bookingRepository.findById(id)
                .map(booking -> {
                    if (is2HoursBefore(booking.getBooking_date())) {
                        Hotel hotel = hotelRepository.findById(booking.getHotel().getId()).orElse(null);
                        if (hotel == null) {
                            return null;
                        }
                        for (Room r: rooms) {
                            r.setHotel(hotel);
                        }
                        for(Room r: booking.getRooms()) {
                            r.setIsAvailable(true);
                        }
                        Booking savedBooking = this.bookingRepository.save(booking);
                        savedBooking.setRooms(rooms);
                        return this.bookingRepository.save(savedBooking);
                    }
                    return null;
                }).orElse(null);
    }


    public String deleteById(Long id) {
        return this.bookingRepository.findById(id).map(booking -> {
            if (is2HoursBefore(booking.getBooking_date())) {
                for (Room room : booking.getRooms()) {
                    room.setIsAvailable(true);
                    this.roomRepository.save(room);
                }
                this.bookingRepository.deleteById(id);
                return "ok";
            }
            return "time-exceeded";
        }).orElse("not-found");
    }


    private boolean is2HoursBefore(Timestamp booking_date_time) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime bookingDateTime = booking_date_time.toLocalDateTime();

        if (currentDateTime.isBefore(bookingDateTime)) {
            Duration duration = Duration.between(currentDateTime, bookingDateTime);
            return duration.toHours() > 2;
        }
        return false;
    }
}
