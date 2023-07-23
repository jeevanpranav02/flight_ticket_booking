package com.round.airplaneticketbooking.service;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.round.airplaneticketbooking.constants.response.AddFlightResponseDTO;
import com.round.airplaneticketbooking.model.Flight;
import com.round.airplaneticketbooking.repository.FlightRepository;
import com.round.airplaneticketbooking.util.JwtService;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final JwtService jwtService;

    @Autowired
    public FlightService(FlightRepository flightRepository, JwtService jwtService) {
        this.flightRepository = flightRepository;
        this.jwtService = jwtService;
    }

    public List<Flight> getFlights(String token, Principal principal) {
        
        List<Flight> flights = null;
        if (jwtService.isTokenValid(token)) {
            return flightRepository.findAll();
        }
        return flights;
    }


    public AddFlightResponseDTO mapToAddFlightResponseDTO(Flight flight) {
        AddFlightResponseDTO responseDTO = AddFlightResponseDTO.builder().id(flight.getId())
                .airline(flight.getAirline())
                .departureDateTime(flight.getDepartureAirport())
                .departureAirport(flight.getDepartureAirport())
                .arrivalAirport(flight.getArrivalAirport()).timeOfFlight(flight.getTimeOfFlight())
                .price(flight.getPrice()).maximumSeats(flight.getMaximumSeats())
                .availableSeats(flight.getAvailableSeats())
                .isActive(flight.isActive())
                .build();
        return responseDTO;
    }

}
