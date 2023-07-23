//package com.round.airplaneticketbooking.controller;
//
//import java.security.Principal;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import com.round.airplaneticketbooking.model.Booking;
//import com.round.airplaneticketbooking.model.Flight;
//import com.round.airplaneticketbooking.service.CustomerService;
//import com.round.airplaneticketbooking.service.FlightService;
//
//@RestController
//@RequestMapping("/api/v1/customer")
//public class CustomerController {
//    private final CustomerService customerService;
//    private final FlightService flightService;
//
//    @Autowired
//    public CustomerController(CustomerService customerService, FlightService flightService) {
//        this.customerService = customerService;
//        this.flightService = flightService;
//    }
//
//
//    @GetMapping("/flights")
//    public ResponseEntity<?> getFlights(@RequestHeader("Authorization") String token,
//            Principal principal) {
//        List<Flight> flights = flightService.getFlights(token, principal);
//        if (flights.isEmpty()) {
//            return ResponseEntity.badRequest().build();
//        }
//        return ResponseEntity.ok(flights);
//    }
//
//    @GetMapping("/bookings")
//    public ResponseEntity<List<Booking>> getBookings(@RequestHeader("Authorization") String token) {
//        List<Booking> bookings = customerService.getBookings(token);
//        return ResponseEntity.ok(bookings);
//    }
//
//    @PostMapping("/bookings")
//    public ResponseEntity<String> bookFlight(@RequestHeader("Authorization") String token,
//            @RequestParam Long flightId, @RequestParam int numberOfSeats) {
//        boolean bookingSuccess = customerService.bookFlight(token, flightId, numberOfSeats);
//        if (bookingSuccess) {
//            return ResponseEntity.ok("Booking successful");
//        } else {
//            return ResponseEntity.badRequest()
//                    .body("Booking failed. Insufficient seats available.");
//        }
//    }
//
//    // @GetMapping("/logout")
//    // public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
//    // customerService.logout(token);
//    // return ResponseEntity.ok("Logged out successfully");
//    // }
//}
