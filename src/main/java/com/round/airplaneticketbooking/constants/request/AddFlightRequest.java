package com.round.airplaneticketbooking.constants.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AddFlightRequest {
    private String airline;
    private LocalDateTime departureDateTime;
    private String departureAirport;
    private Double timeOfFlight;
    private String arrivalAirport;
    private BigDecimal price;
    private int maximumSeats;
    private int availableSeats;
    private boolean isActive;
}
