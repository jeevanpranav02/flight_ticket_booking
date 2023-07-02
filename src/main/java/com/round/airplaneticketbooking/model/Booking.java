package com.round.airplaneticketbooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue
    private Long bookingId;

    private String customerEmail;

    private Long flightId;

    private int numberOfSeats;
    private BigDecimal totalPrice;
    private LocalDateTime bookingDateTime;
    private LocalDateTime departureDateTime;
}
