package com.round.airplaneticketbooking.admin;

import com.round.airplaneticketbooking.admin.auth.AdminAuthenticationService;
import com.round.airplaneticketbooking.enumsAndTemplates.AuthenticationToken;
import com.round.airplaneticketbooking.enumsAndTemplates.LoginRequest;
import com.round.airplaneticketbooking.exception.CustomAuthenticationException;
import com.round.airplaneticketbooking.flight.Flight;
import com.round.airplaneticketbooking.flight.FlightRepository;
import com.round.airplaneticketbooking.booking.Booking;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    private final FlightRepository flightRepository;
    private final AdminAuthenticationService authenticationService;

    public AdminController(AdminService adminService, FlightRepository flightRepository, AdminAuthenticationService authenticationService) {
        this.adminService = adminService;
        this.flightRepository = flightRepository;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try {
            AuthenticationToken authToken = authenticationService.authenticate(email, password);
            return ResponseEntity.ok(authToken);
        } catch (CustomAuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
    }

    @PostMapping("/flights")
    public ResponseEntity<Flight> addFlight(@RequestBody Flight flight) {
        Flight addedFlight = adminService.addFlight(flight);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedFlight);
    }

    @DeleteMapping("/flights/{flightId}")
    public ResponseEntity<Void> removeFlight(@PathVariable Long flightId) {
        adminService.removeFlight(flightId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bookings/{flightId}/{flightTime}")
    public ResponseEntity<List<Booking>> getBookingsByFlightAndDepartureDateTime(@PathVariable Long flightId,
                                                                                 @PathVariable LocalDateTime flightTime) {
        Optional<Flight> optionalFlight = flightRepository.findByFlightId(flightId);
        if (optionalFlight.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Flight flight = optionalFlight.get();
        List<Booking> bookings = adminService.getBookingsByFlightAndDepartureDateTime(flight, flightTime);
        return ResponseEntity.ok(bookings);
    }

}
