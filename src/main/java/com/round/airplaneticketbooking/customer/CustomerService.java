package com.round.airplaneticketbooking.customer;

import com.round.airplaneticketbooking.booking.Booking;
import com.round.airplaneticketbooking.booking.BookingRepository;
import com.round.airplaneticketbooking.enumsAndTemplates.AuthenticationToken;
import com.round.airplaneticketbooking.flight.Flight;
import com.round.airplaneticketbooking.flight.FlightRepository;
import com.round.airplaneticketbooking.util.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final Set<String> invalidatedTokens;

    public CustomerService(CustomerRepository customerRepository, FlightRepository flightRepository,
                           BookingRepository bookingRepository, JwtTokenProvider jwtTokenProvider) {
        this.customerRepository = customerRepository;
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.invalidatedTokens = new HashSet<>();
    }

    Customer currentUser = null;

    public Customer signup(Customer customer) {
        currentUser = customerRepository.save(customer);
        return currentUser;
    }

    public AuthenticationToken login(String email, String password) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            if (customer.getPassword().equals(password)) {
                String token = jwtTokenProvider.generateToken(customer.getCustomerId());
                return new AuthenticationToken(token);
            }
        }
        throw new RuntimeException("Invalid credentials");
    }

    public List<Booking> getBookings(String token) {
        if (jwtTokenProvider.validateToken(token, currentUser.getCustomerId())) {
            Long customerId = jwtTokenProvider.getUserIdFromToken(token);
            Customer loggedInCustomer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            return bookingRepository.findByCustomer(loggedInCustomer);
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
                booking.setFlight(flight);
                booking.setCustomer(loggedInCustomer);
                booking.setNumberOfSeats(numberOfSeats);
                booking.setBookingDateTime(LocalDateTime.now());

                int totalPrice = flight.getPrice() * numberOfSeats;
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
