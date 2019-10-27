package com.danielgkneto.mcjavabc.flightbooking.dao;

import com.danielgkneto.mcjavabc.flightbooking.entity.Flight;
import org.springframework.data.repository.CrudRepository;
import java.util.Set;

public interface IFlightRepository extends CrudRepository<Flight, Long> {
    Set<Flight> findAll();
    Set<Flight> findAllByDepartureAirportContainingIgnoreCaseAndArrivalAirportContainingIgnoreCase(String departureAirport, String ArrivalAirport);
}
