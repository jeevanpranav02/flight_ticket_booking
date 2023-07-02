package com.round.airplaneticketbooking.service;

import com.round.airplaneticketbooking.model.Booking;
import com.round.airplaneticketbooking.repository.BookingRepository;
import com.round.airplaneticketbooking.constants.response.AuthenticationToken;
import com.round.airplaneticketbooking.constants.request.RegisterRequest;
import com.round.airplaneticketbooking.model.Customer;
import com.round.airplaneticketbooking.model.Flight;
import com.round.airplaneticketbooking.repository.CustomerRepository;
import com.round.airplaneticketbooking.repository.FlightRepository;
import com.round.airplaneticketbooking.util.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthenticationService userAuthenticationService;
    private final Set<String> invalidatedTokens;

    public CustomerService(CustomerRepository customerRepository, FlightRepository flightRepository,
                           BookingRepository bookingRepository, JwtTokenProvider jwtTokenProvider,
                           UserAuthenticationService userAuthenticationService) {
        this.customerRepository = customerRepository;
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userAuthenticationService = userAuthenticationService;
        this.invalidatedTokens = new HashSet<>();
    }

    Customer currentUser = null;

    public AuthenticationToken signup(RegisterRequest registerRequest) {
        AuthenticationToken token = userAuthenticationService.register(registerRequest);
        return token;
    }

    public AuthenticationToken login(String email, String password) {
        AuthenticationToken token = userAuthenticationService.authenticate(email, password);
        return token;
    }

    public List<Booking> getBookings(String token) {
        if (jwtTokenProvider.validateToken(token, currentUser.getCustomerId())) {
            Long customerId = jwtTokenProvider.getUserIdFromToken(token);
            Customer loggedInCustomer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            return bookingRepository.findByCustomerEmail(loggedInCustomer.getEmail());
        }
        throw new RuntimeException("Invalid token");
    }

    public boolean bookFlight(String token, Long flightId, int numberOfSeats) {
        if (jwtTokenProvider.validateToken(token, currentUser.getCustomerId())) {
            Long customerId = jwtTokenProvider.getUserIdFromToken(token);
            Customer loggedInCustomer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            Flight flight = flightRepository.findById(flightId)
                    .orElseThrow(() -> new RuntimeException("Flight not found"));

            if (flight.getAvailableSeats() >= numberOfSeats) {
                flight.setAvailableSeats(flight.getAvailableSeats() - numberOfSeats);
                Booking booking = new Booking();
                booking.setFlightId(flight.getFlightId());
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

    public void logout(String token) {
        invalidatedTokens.add(token);
    }

    public boolean isTokenValid(String token) {
        return !invalidatedTokens.contains(token) && jwtTokenProvider.validateToken(token, currentUser.getCustomerId());
    }
}
