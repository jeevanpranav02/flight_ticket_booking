package com.round.airplaneticketbooking.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.round.airplaneticketbooking.model.Customer;
import com.round.airplaneticketbooking.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.round.airplaneticketbooking.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomer(Customer customer);

    @Query(
            "SELECT b FROM Booking b " +
                    "WHERE b.flight = :flight"
    )
    List<Booking> findByFlight(Flight flight);

    @Query(
            "SELECT b FROM Booking b " +
            "WHERE DATE(b.departureDateTime) = DATE(:departureDateTime)"
    )
    List<Booking> findByDepartureDateTime(@Param("departureDateTime") LocalDateTime departureDateTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.flight = :flight"
    )
    List<Booking> findBookingByFlight(@Param("flight") Flight flight);
}
