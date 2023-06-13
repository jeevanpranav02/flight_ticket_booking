package com.round.airplaneticketbooking.admin;

import com.round.airplaneticketbooking.booking.Booking;
import com.round.airplaneticketbooking.flight.Flight;
import com.round.airplaneticketbooking.flight.FlightRepository;
import com.round.airplaneticketbooking.booking.BookingRepository;
import com.round.airplaneticketbooking.util.JwtTokenProvider;
import com.round.airplaneticketbooking.config.JwtConfig;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
@Data
public class AdminService {
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final JwtTokenProvider tokenProvider;

    public AdminService(FlightRepository flightRepository, BookingRepository bookingRepository, JwtConfig jwtConfig) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.tokenProvider = new JwtTokenProvider(jwtConfig);
    }


    public Flight addFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public void removeFlight(Long flightId) {
        flightRepository.deleteById(flightId);
    }

    public List<Booking> getBookingsByFlightAndDepartureDateTime(Flight flight, LocalDateTime departureDateTime) {
        LocalDateTime departureDateTimeStart = departureDateTime.with(LocalTime.MIN);
        LocalDateTime departureDateTimeEnd = departureDateTime.with(LocalTime.MAX);

        List<Flight> flightFromRepo = flightRepository.findByDepartureDateTimeBetween(departureDateTimeStart, departureDateTimeEnd);
        if (!flightFromRepo.isEmpty()) {
            flight = flightFromRepo.get(0);
        }

        if (flight != null) {
            return bookingRepository.findByFlightAndDepartureDateTime(flight, departureDateTime);
        }

        return Collections.emptyList();
    }

}
