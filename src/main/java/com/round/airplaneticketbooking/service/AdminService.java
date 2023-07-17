package com.round.airplaneticketbooking.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.round.airplaneticketbooking.config.JwtConfig;
import com.round.airplaneticketbooking.constants.request.AddFlightRequest;
import com.round.airplaneticketbooking.constants.request.RegisterRequest;
import com.round.airplaneticketbooking.constants.response.AuthenticationToken;
import com.round.airplaneticketbooking.model.Admin;
import com.round.airplaneticketbooking.model.Booking;
import com.round.airplaneticketbooking.model.Flight;
import com.round.airplaneticketbooking.repository.BookingRepository;
import com.round.airplaneticketbooking.repository.FlightRepository;
import com.round.airplaneticketbooking.util.JwtService;
import lombok.Data;

@Service
@Data
public class AdminService {
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final JwtService tokenProvider;
    private final AdminAuthenticationService adminAuthenticationService;

    public AdminService(FlightRepository flightRepository, BookingRepository bookingRepository,
            JwtConfig jwtConfig, AdminAuthenticationService adminAuthenticationService) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.tokenProvider = new JwtService(jwtConfig);
        this.adminAuthenticationService = adminAuthenticationService;
    }

    public AuthenticationToken signup(RegisterRequest registerRequest) {
        AuthenticationToken token = adminAuthenticationService.register(registerRequest);
        return token;
    }

    public AuthenticationToken login(String email, String password) {
        AuthenticationToken token = adminAuthenticationService.authenticate(email, password);
        return token;
    }


    public Flight addFlight(AddFlightRequest request, Optional<Admin> authenticatedAdmin) {
        Flight flight = Flight.builder().airline(request.getAirline())
                .arrivalAirport(request.getArrivalAirport())
                .admin(authenticatedAdmin.orElse(null))
                .departureDateTime(request.getDepartureDateTime())
                .departureAirport(request.getDepartureAirport())
                .timeOfFlight(request.getTimeOfFlight()).price(request.getPrice())
                .maximumSeats(request.getMaximumSeats()).availableSeats(request.getAvailableSeats())
                .build();

        return flightRepository.save(flight);
    }

    public void removeFlight(Long flightId) {
        flightRepository.deleteById(flightId);
    }

    public List<Flight> getFlightsByDepartureDate(LocalDateTime departureDateTime) {
        LocalDateTime departureDateTimeStart = departureDateTime.with(LocalTime.MIN);
        LocalDateTime departureDateTimeEnd = departureDateTime.with(LocalTime.MAX);

        List<Flight> flightList = flightRepository
                .findByDepartureDateTimeBetween(departureDateTimeStart, departureDateTimeEnd);

        if (flightList.isEmpty()) {
            return Collections.emptyList();
        }

        return flightList;
    }

    public List<Booking> getBookingsByFlightId(Long flightId) {
        if (flightId != null) {
            return bookingRepository.findByFlightId(flightId);
        }

        return Collections.emptyList();
    }

}
