package com.round.airplaneticketbooking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.round.airplaneticketbooking.model.Admin;
import com.round.airplaneticketbooking.model.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findById(Long id);

    @Query("SELECT f FROM Flight f WHERE f.admin = :admin")
    List<Flight> findByAddedByAdmin(Admin admin);

    @Query("SELECT f FROM Flight f " + "WHERE f.availableSeats >= :availableSeats")
    List<Flight> findByAvailableSeats(int availableSeats);

    @Query("SELECT f FROM Flight f " + "WHERE f.departureDateTime "
            + "BETWEEN :departureDateTimeStart AND :departureDateTimeEnd")
    List<Flight> findByDepartureDateTimeBetween(LocalDateTime departureDateTimeStart,
            LocalDateTime departureDateTimeEnd);

}
