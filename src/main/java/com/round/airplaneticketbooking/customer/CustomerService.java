package com.round.airplaneticketbooking.customer;

import com.round.airplaneticketbooking.booking.Booking;
import com.round.airplaneticketbooking.booking.BookingRepository;
import com.round.airplaneticketbooking.flight.Flight;
import com.round.airplaneticketbooking.flight.FlightRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Service
public class CustomerService {
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    public CustomerService(FlightRepository flightRepository, BookingRepository bookingRepository) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Flight> searchFlightsByDateTime(LocalDateTime departureDateTime) {
        LocalDateTime departureDateTimeStart = departureDateTime.with(LocalTime.MIN);
        LocalDateTime departureDateTimeEnd = departureDateTime.with(LocalTime.MAX);
        return flightRepository.findByDepartureDateTimeBetween(departureDateTimeStart, departureDateTimeEnd);
    }

    public boolean bookFlight(Flight flight, Customer customer, int numberOfSeats) {
        if (flight.getAvailableSeats() >= numberOfSeats) {
            flight.setAvailableSeats(flight.getAvailableSeats() - numberOfSeats);
            Booking booking = new Booking();
            booking.setFlight(flight);
            booking.setCustomer(customer);
            booking.setNumberOfSeats(numberOfSeats);
            booking.setBookingDateTime(LocalDateTime.now());

            int totalPrice = flight.getPrice() * numberOfSeats;
            booking.setTotalPrice(totalPrice);

            bookingRepository.save(booking);
            flightRepository.save(flight);
            return true;
        }
        return false;
    }


    public List<Booking> getBookingsByCustomer(Customer customer) {
        return bookingRepository.findByCustomer(customer);
    }
}
