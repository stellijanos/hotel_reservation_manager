import { Room } from "./room";

export interface Hotel {
    id: Number, 
    name: String,
    latitude: Number,
    longitude: Number, 
    rooms: Room[]
}
