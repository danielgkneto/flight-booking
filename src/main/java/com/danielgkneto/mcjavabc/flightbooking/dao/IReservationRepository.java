package com.danielgkneto.mcjavabc.flightbooking.dao;

import com.danielgkneto.mcjavabc.flightbooking.entity.Reservation;
import com.danielgkneto.mcjavabc.flightbooking.entity.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Set;

public interface IReservationRepository extends CrudRepository<Reservation, Long> {
    Set<Reservation> findAll();
    Set<Reservation> findByUser(User user);
}
