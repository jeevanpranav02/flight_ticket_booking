package com.round.airplaneticketbooking.service;

import com.round.airplaneticketbooking.constants.request.RegisterRequest;
import com.round.airplaneticketbooking.constants.response.AuthenticationToken;
import com.round.airplaneticketbooking.model.Booking;
import com.round.airplaneticketbooking.model.Customer;
import com.round.airplaneticketbooking.model.Flight;
import com.round.airplaneticketbooking.repository.BookingRepository;
import com.round.airplaneticketbooking.repository.CustomerRepository;
import com.round.airplaneticketbooking.repository.FlightRepository;
import com.round.airplaneticketbooking.util.JwtService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final JwtService jwtService;
    private final CustomerAuthenticationService customerAuthenticationService;

    public CustomerService(CustomerRepository customerRepository, FlightRepository flightRepository,
                           BookingRepository bookingRepository, JwtService jwtService,
                           CustomerAuthenticationService customerAuthenticationService) {
        this.customerRepository = customerRepository;
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.jwtService = jwtService;
        this.customerAuthenticationService = customerAuthenticationService;
    }

    Customer currentUser = null;

    public AuthenticationToken signup(RegisterRequest registerRequest) {
        return customerAuthenticationService.register(registerRequest);
    }

    public AuthenticationToken login(String email, String password) {
            return customerAuthenticationService.authenticate(email, password);
    }

    public List<Booking> getBookings(String token) {
        if (jwtService.isTokenValid(token, currentUser)) {
            Long customerId = jwtService.getUserIdFromToken(token);
            Customer loggedInCustomer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            return bookingRepository.findByCustomerEmail(loggedInCustomer.getEmail());
        }
        throw new RuntimeException("Invalid token");
    }

    public boolean bookFlight(String token, Long flightId, int numberOfSeats) {
        if (jwtService.isTokenValid(token, currentUser)) {
            Long customerId = jwtService.getUserIdFromToken(token);
            Customer loggedInCustomer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            Flight flight = flightRepository.findById(flightId)
                    .orElseThrow(() -> new RuntimeException("Flight not found"));

            if (flight.getAvailableSeats() >= numberOfSeats) {
                flight.setAvailableSeats(flight.getAvailableSeats() - numberOfSeats);
                Booking booking = new Booking();
                booking.setFlightId(flight.getId());
                booking.setCustomerEmail(loggedInCustomer.getEmail());
                booking.setNumberOfSeats(numberOfSeats);
                booking.setBookingDateTime(LocalDateTime.now());

                BigDecimal totalPrice = flight.getPrice().multiply(BigDecimal.valueOf(numberOfSeats));
                booking.setTotalPrice(totalPrice);

                bookingRepository.save(booking);
                flightRepository.save(flight);
                return true;
            }
            return false;
        }
        throw new RuntimeException("Invalid token");
    }

}
