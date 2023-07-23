package com.round.airplaneticketbooking.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.round.airplaneticketbooking.exception.CustomAuthenticationException;
import org.springframework.stereotype.Service;
import com.round.airplaneticketbooking.constants.request.AddFlightRequest;
import com.round.airplaneticketbooking.constants.request.RegisterRequest;
import com.round.airplaneticketbooking.constants.response.AddFlightResponseDTO;
import com.round.airplaneticketbooking.constants.response.AuthenticationToken;
import com.round.airplaneticketbooking.model.Admin;
import com.round.airplaneticketbooking.model.Booking;
import com.round.airplaneticketbooking.model.Flight;
import com.round.airplaneticketbooking.repository.AdminRepository;
import com.round.airplaneticketbooking.repository.BookingRepository;
import com.round.airplaneticketbooking.repository.FlightRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final AdminRepository adminRepository;
    private final FlightService flightService;
    private final AdminAuthenticationService adminAuthenticationService;

    public AuthenticationToken signup(RegisterRequest registerRequest) {
        return adminAuthenticationService.register(registerRequest);
    }

    public AuthenticationToken login(String email, String password) {
        return adminAuthenticationService.authenticate(email, password);
    }

    public AddFlightResponseDTO addFlight(AddFlightRequest request, Principal principal) {
        Optional<Admin> authenticatedAdmin = adminRepository.findByEmail(principal.getName());
        Admin admin = authenticatedAdmin.orElseThrow(()-> new CustomAuthenticationException("Admin Exception"));
        Flight flight = Flight.builder().airline(request.getAirline())
                .admin(admin)
                .arrivalAirport(request.getArrivalAirport())
                .departureDateTime(request.getDepartureDateTime())
                .departureAirport(request.getDepartureAirport())
                .timeOfFlight(request.getTimeOfFlight()).price(request.getPrice())
                .maximumSeats(request.getMaximumSeats()).availableSeats(request.getAvailableSeats())
                .isActive(request.isActive())
                .build();
        
        flightRepository.save(flight);
        return flightService.mapToAddFlightResponseDTO(flight);
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
        Flight flight = flightRepository.findById(flightId)
                                        .orElse(null);
        if (flight != null) {
            return bookingRepository.findByFlight(flight);
        }

        return Collections.emptyList();
    }

}
