package com.danielgkneto.mcjavabc.flightbooking.dao;

import com.danielgkneto.mcjavabc.flightbooking.entity.Passenger;
import com.danielgkneto.mcjavabc.flightbooking.entity.Reservation;
import org.springframework.data.repository.CrudRepository;
import java.util.Set;

public interface IPassengerRepository extends CrudRepository<Passenger, Long> {
    Set<Passenger> findAll();
    Set<Passenger> findAllByReservation(Reservation reservation);
}
