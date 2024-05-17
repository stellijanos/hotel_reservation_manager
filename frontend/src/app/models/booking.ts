import { Hotel } from "./hotel";
import { Room } from "./room";

export interface Booking {
    id: Number, 
    firstname: String, 
    lastname: String, 
    email: String, 
    phone: String, 
    price: Number, 
    booking_date: Date,
    hotel: Hotel,
    rooms: Room[]
}
