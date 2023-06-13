package com.round.airplaneticketbooking.flight;

import com.round.airplaneticketbooking.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findByFlightId(Long flightId);

    List<Flight> findByAddedByAdmin(Admin admin);

    List<Flight> findByAvailableSeats(int availableSeats);

    List<Flight> findByDepartureDateTimeBetween(LocalDateTime departureDateTimeStart, LocalDateTime departureDateTimeEnd);


}
