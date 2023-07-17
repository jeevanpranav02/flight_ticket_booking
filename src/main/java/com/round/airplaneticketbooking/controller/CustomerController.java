package com.round.airplaneticketbooking.controller;

import com.round.airplaneticketbooking.model.Booking;
import com.round.airplaneticketbooking.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getBookings(@RequestHeader("Authorization") String token) {
        List<Booking> bookings = customerService.getBookings(token);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("/bookings")
    public ResponseEntity<String> bookFlight(@RequestHeader("Authorization") String token,
                                             @RequestParam Long flightId, @RequestParam int numberOfSeats) {
        boolean bookingSuccess = customerService.bookFlight(token, flightId, numberOfSeats);
        if (bookingSuccess) {
            return ResponseEntity.ok("Booking successful");
        } else {
            return ResponseEntity.badRequest().body("Booking failed. Insufficient seats available.");
        }
    }

//    @GetMapping("/logout")
//    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
//        customerService.logout(token);
//        return ResponseEntity.ok("Logged out successfully");
//    }
}
