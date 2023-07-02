package com.round.airplaneticketbooking.repository;

import com.round.airplaneticketbooking.model.Admin;
import com.round.airplaneticketbooking.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findById(Long id);

    @Query("SELECT f FROM Flight f WHERE f.adminId = :adminId")
    List<Flight> findByAddedByAdminId(Long adminId);

    @Query(
            "SELECT f FROM Flight f " +
            "WHERE f.availableSeats >= :availableSeats"
    )
    List<Flight> findByAvailableSeats(int availableSeats);

    @Query(
            "SELECT f FROM Flight f " +
            "WHERE f.departureDateTime " +
            "BETWEEN :departureDateTimeStart AND :departureDateTimeEnd"
    )
    List<Flight> findByDepartureDateTimeBetween(LocalDateTime departureDateTimeStart, LocalDateTime departureDateTimeEnd);

}
