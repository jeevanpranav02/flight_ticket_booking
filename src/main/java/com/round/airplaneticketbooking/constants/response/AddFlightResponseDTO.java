package com.round.airplaneticketbooking.constants.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddFlightResponseDTO {
    private Long id;
    private String airline;
    private String departureDateTime;
    private String departureAirport;
    private Double timeOfFlight;
    private String arrivalAirport;
    private BigDecimal price;
    private int maximumSeats;
    private int availableSeats;
}
