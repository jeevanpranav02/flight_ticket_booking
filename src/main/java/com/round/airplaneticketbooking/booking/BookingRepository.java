package com.round.airplaneticketbooking.booking;

import com.round.airplaneticketbooking.customer.Customer;
import com.round.airplaneticketbooking.flight.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomer(Customer customer);

    List<Booking> findByFlight(Flight flight);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.flight = :flight " +
            "AND FUNCTION('DATE', b.departureDateTime) = FUNCTION('DATE', :departureDateTime) " +
            "LIMIT 20")
    List<Booking> findByFlightAndDepartureDateTime(
            @Param("flight") Flight flight,
            @Param("departureDateTime") LocalDateTime departureDateTime);
}
