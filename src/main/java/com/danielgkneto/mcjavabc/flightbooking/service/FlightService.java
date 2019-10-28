package com.danielgkneto.mcjavabc.flightbooking.service;

import com.danielgkneto.mcjavabc.flightbooking.dao.IFlightRepository;
import com.danielgkneto.mcjavabc.flightbooking.entity.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class FlightService {

    @Autowired
    IFlightRepository flightRepository;

    public Set<String> getAirportList() {
        Set<Flight> flights = flightRepository.findAll();
        Set<String> airports = new HashSet<>();

        for(Flight flight : flights) {
            airports.add(flight.getDepartureAirport());
            airports.add(flight.getArrivalAirport());
        }

        return airports;
    }
}
