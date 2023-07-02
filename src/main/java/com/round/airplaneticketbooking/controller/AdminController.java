package com.round.airplaneticketbooking.controller;

import com.round.airplaneticketbooking.model.Flight;
import com.round.airplaneticketbooking.repository.FlightRepository;
import com.round.airplaneticketbooking.model.Booking;
import com.round.airplaneticketbooking.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class AdminController {
    private final AdminService adminService;
    private final FlightRepository flightRepository;

    public AdminController(
            AdminService adminService,
            FlightRepository flightRepository
    ) {
        this.adminService = adminService;
        this.flightRepository = flightRepository;
    }

    @PostMapping("/flights")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Flight> addFlight(@RequestBody Flight flight) {
        Flight addedFlight = adminService.addFlight(flight);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedFlight);
    }

    @DeleteMapping("/flights/{flightId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> removeFlight(@PathVariable Long flightId) {
        adminService.removeFlight(flightId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bookings/{flightId}/{flightTime}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Booking>> getBookingsByFlightAndDepartureDateTime(@PathVariable Long flightId,
                                                                                 @PathVariable LocalDateTime flightTime) {
        Optional<Flight> optionalFlight = flightRepository.findById(flightId);
        if (optionalFlight.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Flight flight = optionalFlight.get();
        List<Booking> bookings = adminService.getBookingsByFlightIdAndDepartureDateTime(flight.getId(), flightTime);
        return ResponseEntity.ok(bookings);
    }

}
