package com.round.airplaneticketbooking.service;

import org.springframework.stereotype.Service;
import com.round.airplaneticketbooking.model.Flight;
import com.round.airplaneticketbooking.constants.response.AddFlightResponseDTO;
@Service
public class FlightService {
    public AddFlightResponseDTO mapToAddFlightResponseDTO(Flight flight) {
        AddFlightResponseDTO responseDTO = AddFlightResponseDTO.builder()
                                           .id(flight.getId())
                                           .airline(flight.getAirline())
                                           .departureDateTime(flight.getDepartureAirport().toString())
                                           .departureAirport(flight.getDepartureAirport())
                                           .arrivalAirport(flight.getArrivalAirport())
                                           .timeOfFlight(flight.getTimeOfFlight())
                                           .price(flight.getPrice())
                                           .maximumSeats(flight.getMaximumSeats())
                                           .availableSeats(flight.getAvailableSeats())
                                           .build();

        System.out.println(flight.toString());
        return responseDTO;
    }
}
