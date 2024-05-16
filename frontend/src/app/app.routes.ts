import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { BookingComponent } from './booking/booking.component';

export const routes: Routes = [
    {path: '', component: HomeComponent},
    {path: 'bookings', component: BookingComponent}
];
