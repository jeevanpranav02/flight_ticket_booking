package com.round.airplaneticketbooking.service;

import com.round.airplaneticketbooking.model.Booking;
import com.round.airplaneticketbooking.constants.response.AuthenticationToken;
import com.round.airplaneticketbooking.constants.request.RegisterRequest;
import com.round.airplaneticketbooking.model.Flight;
import com.round.airplaneticketbooking.repository.FlightRepository;
import com.round.airplaneticketbooking.repository.BookingRepository;
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
    private final AdminAuthenticationService adminAuthenticationService;

    public AdminService(
            FlightRepository flightRepository,
            BookingRepository bookingRepository,
            JwtConfig jwtConfig,
            AdminAuthenticationService adminAuthenticationService) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.tokenProvider = new JwtTokenProvider(jwtConfig);
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


    public Flight addFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public void removeFlight(Long flightId) {
        flightRepository.deleteById(flightId);
    }

    public List<Booking> getBookingsByFlightIdAndDepartureDateTime(Long flightId, LocalDateTime departureDateTime) {
        LocalDateTime departureDateTimeStart = departureDateTime.with(LocalTime.MIN);
        LocalDateTime departureDateTimeEnd = departureDateTime.with(LocalTime.MAX);

        List<Flight> flightFromRepo = flightRepository.findByDepartureDateTimeBetween(departureDateTimeStart, departureDateTimeEnd);
        if (!flightFromRepo.isEmpty()) {
            flightId = flightFromRepo.get(0).getFlightId();
        }

        if (flightId != null) {
            return bookingRepository.findByFlightAndDepartureDateTime(flightId, departureDateTime);
        }

        return Collections.emptyList();
    }

}
