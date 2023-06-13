package com.round.airplaneticketbooking.booking;

import com.round.airplaneticketbooking.customer.Customer;
import com.round.airplaneticketbooking.flight.Flight;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    private int numberOfSeats;
    private int totalPrice;
    private LocalDateTime bookingDateTime;
    private LocalDateTime departureDateTime;
}
