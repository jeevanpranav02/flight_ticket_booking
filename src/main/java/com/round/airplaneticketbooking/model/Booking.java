package com.round.airplaneticketbooking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    private String customerEmail;

    private Long flightId;

    private int numberOfSeats;
    private BigDecimal totalPrice;
    private LocalDateTime bookingDateTime;
    private LocalDateTime departureDateTime;
}
