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


    /**
     * Creates a new booking
     *
     * @param hotelId - id of the hotel in which we have the booked rooms
     * @param booking - the booking we want to create, it contains user info and the rooms
     * @return - the saved booking is returned as confirmation of the successfully created booking
     */
    public Booking create(Long hotelId, Booking booking) {

        // if hotel does not exist, booking cannot be created
        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        if (hotel == null) {
            return null;
        }

        // otherwise, the book will have the hotel as attribute
        booking.setHotel(hotel);

        // get all the room id's that is recieved with the booking itself
        List<Long> roomNumbers = booking.getRooms().stream().map(Room::getId).collect(Collectors.toList());

        // get all rooms by their id's and verify if their belong to the actual hotel and also their availability
        // then the room availability is set to false
        List<Room> rooms = ((List<Room>) roomRepository.findAllById(roomNumbers))
                .stream()
                .filter(room -> room.getHotel().getId().equals(hotelId) && room.getIsAvailable())
                .peek(room -> room.setIsAvailable(false))
                .toList();

        // set the rooms for the booking
        booking.setRooms(rooms);

        // save the booking and also return it
        return this.bookingRepository.save(booking);
    }


    /**
     * retrieve all the bookings
     * @return - a list of all bookings
     */
    public Iterable<Booking> getAll() {
        return this.bookingRepository.findAll();
    }


    /**
     * retrieve booking by provided id as parameter
     * @param id - id of the booking we want to retrieve
     * @return - the booking that is found or null otherwise
     */
    public Booking getById(Long id) {
        return this.bookingRepository.findById(id).orElse(null);
    }


    /**
     * change the booked rooms for a specific booking
     * @param id - id of the booking
     * @param rooms - the rooms we want to have in the booking
     * @return - updated booking with changed booked rooms
     */
    public Booking changeBookedRooms(Long id, List<Room> rooms) {

        // find booking
        return this.bookingRepository.findById(id)
                .map(booking -> { // if it's found

                    // verify that the check-in is 2 hours before the current time
                    if (is2HoursBefore(booking.getBooking_date())) {

                        // set available for all the rooms
                        for(Room r: booking.getRooms()) {
                            r.setIsAvailable(true);
                        }

                        // get all the room id's that is recieved with the booking itself
                        List<Long> roomNumbers = booking.getRooms().stream().map(Room::getId).collect(Collectors.toList());

                        // get all rooms by their id's and verify if their belong to the actual hotel and also their availability
                        // then the room availability is set to false
                        List<Room> existing_rooms = ((List<Room>) roomRepository.findAllById(roomNumbers))
                                .stream()
                                .filter(room -> room.getHotel().getId().equals(booking.getHotel().getId()) && room.getIsAvailable())
                                .peek(room -> room.setIsAvailable(false))
                                .toList();

                        // set the rooms for the booking
                        booking.setRooms(existing_rooms);

                        // save and also return the updated booking
                        return this.bookingRepository.save(booking);
                    }
                    return null; // if there are less then 2 hours before check-in return null
                }).orElse(null); // if booking is not found, return null
    }


    /**
     * deletes a booking by its id
     * @param id - id of the booking
     * @return - confirmation message
     */
    public String deleteById(Long id) {
        // search for the booking by the provided id
        return this.bookingRepository.findById(id).map(booking -> { // if booking was found

            // verify that there are more than 2 hours before the check-in
            if (is2HoursBefore(booking.getBooking_date())) {

                // set for all the rooms the availability to true
                for (Room room : booking.getRooms()) {
                    room.setIsAvailable(true);

                    // update the rooms in the database
                    this.roomRepository.save(room);
                }
                // delete booking
                this.bookingRepository.deleteById(id);
                return "ok";
            }
            return "time-exceeded"; // if more than 2 hours before check-in
        }).orElse("not-found"); // if booking was not found by the provided id
    }


    /**
     *
     * @param booking_date_time check-in date and time for the booking
     * @return true if there are more than 2 hours before check-in,, otherwise false
     */
    private boolean is2HoursBefore(Timestamp booking_date_time) {
        LocalDateTime currentDateTime = LocalDateTime.now(); // get current time
        LocalDateTime bookingDateTime = booking_date_time.toLocalDateTime(); // convert into localdatetime

        if (currentDateTime.isBefore(bookingDateTime)) { // if current time is before the check-in date
            Duration duration = Duration.between(currentDateTime, bookingDateTime); // calculate duration
            return duration.toHours() > 2; // returns true if duration is more than 2 hours, otherwise false
        }
        return false; // if current date is after booking date
    }
}
