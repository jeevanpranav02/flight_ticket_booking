package com.round.airplaneticketbooking.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.round.airplaneticketbooking.constants.request.AddFlightRequest;
import com.round.airplaneticketbooking.constants.response.AddFlightResponseDTO;
import com.round.airplaneticketbooking.model.Booking;
import com.round.airplaneticketbooking.model.Flight;
import com.round.airplaneticketbooking.repository.FlightRepository;
import com.round.airplaneticketbooking.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;
    private final FlightRepository flightRepository;

    @PostMapping("/flight")
    public ResponseEntity<AddFlightResponseDTO> addFlight(
            @RequestBody AddFlightRequest flightRequest,
            Principal principal) {
        AddFlightResponseDTO responseDTO = adminService.addFlight(flightRequest, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @DeleteMapping("/flight/{flightId}")
    public ResponseEntity<Void> removeFlight(@PathVariable Long flightId) {
        adminService.removeFlight(flightId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bookings/{flightId}/{flightTime}")
    public ResponseEntity<List<Booking>> getBookingsByFlightId(@PathVariable Long flightId) {
        Optional<Flight> optionalFlight = flightRepository.findById(flightId);
        if (optionalFlight.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Flight flight = optionalFlight.get();
        List<Booking> bookings = adminService.getBookingsByFlightId(flight.getId());
        return ResponseEntity.ok(bookings);
    }

}
