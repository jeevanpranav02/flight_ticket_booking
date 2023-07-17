package com.round.airplaneticketbooking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue
    private Long id;
    private String airline;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    private LocalDateTime departureDateTime;
    private String departureAirport;
    private Double timeOfFlight;
    private String arrivalAirport;
    private BigDecimal price;
    private int maximumSeats;
    private int availableSeats;
}
