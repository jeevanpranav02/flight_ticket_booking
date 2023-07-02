package com.round.airplaneticketbooking.repository;

import com.round.airplaneticketbooking.model.Booking;
import com.round.airplaneticketbooking.model.Customer;
import com.round.airplaneticketbooking.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerEmail(String customerEmail);

    List<Booking> findByFlightId(Long flightId);

    @Query(
            "SELECT b FROM Booking b " +
            "WHERE b.flightId = :flightId AND " +
            "DATE(b.departureDateTime) = DATE(:departureDateTime)"
    )
    List<Booking> findByFlightAndDepartureDateTime(@Param("flightId") Long flightId, @Param("departureDateTime") LocalDateTime departureDateTime);

}
