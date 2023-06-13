package com.round.airplaneticketbooking.flight;

import com.round.airplaneticketbooking.admin.Admin;
import com.round.airplaneticketbooking.booking.Booking;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue
    private Long flightId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminId")
    private Admin addedByAdmin;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    private String airline;
    private LocalDateTime departureDateTime;
    private String departureAirport;
    private Double timeOfFlight;
    private String arrivalAirport;
    private int price;
    private int maximumSeats;
    private int availableSeats;
}
