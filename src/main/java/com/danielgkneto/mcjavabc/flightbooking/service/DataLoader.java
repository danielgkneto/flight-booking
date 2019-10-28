package com.danielgkneto.mcjavabc.flightbooking.service;

import com.danielgkneto.mcjavabc.flightbooking.dao.*;
import com.danielgkneto.mcjavabc.flightbooking.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IRoleRepository roleRepository;

    @Autowired
    IFlightRepository flightRepository;

    @Autowired
    IReservationRepository reservationRepository;

    @Autowired
    IPassengerRepository passengerRepository;

    @Override
    public void run(String... strings) throws Exception {

        if (roleRepository.findAll().size() == 0) {
            roleRepository.save(new Role("USER"));
            roleRepository.save(new Role("ADMIN"));

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(roleRepository.findByName("USER"));

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleRepository.findByName("ADMIN"));
            adminRoles.add(roleRepository.findByName("USER"));

            User user = new User("Admin", "Admin", "admin", "password", "99/99/9999", "admin", "admin@example.com", "999-999-9999", adminRoles);
            userRepository.save(user);

            user = new User("Daniel", "Krause", "danielgkneto", "password", "11/10/1979", "Brazil", "daniel@example.com", "222-222-2222", userRoles);
            userRepository.save(user);

            User user1 = new User("Paul", "Waters", "pwaters", "pwdpw", "05/23/1992", "USA", "pwaters@email.com", "301-525-7896", userRoles);
            userRepository.save(user1);

            User user2 = new User("Jane", "Garten", "jgarten", "pwdjg", "02/17/2001", "USA", "jgarten@email.com", "301-555-6789", userRoles);
            userRepository.save(user2);

            User user3 = new User("Joshua", "Woods", "jwoods", "pwdjw", "09/02/1981", "USA", "jwoods@email.com", "301-555-1234", userRoles);
            userRepository.save(user3);

            User user4 = new User("Admin", "User", "auser", "pwdau", "01/21/2000", "USA", "admin@email.com", "111-555-9999", userRoles);
            userRepository.save(user4);

            User user5 = new User("Donna", "Woods", "dwoods", "pwddw", "08/17/1981", "USA", "dwoods@email.com", "301-555-1235", userRoles);
            userRepository.save(user5);

            User user6 = new User("Ann", "Woods", "awoods", "pwdaw", "03/24/2007", "USA", "awoods@email.com", "301-555-1235", userRoles);
            userRepository.save(user6);

            LocalTime time;
            time = LocalTime.of(9, 45, 00);
            Flight flight1 = new Flight("101", "IAD - Washington, DC - Dulles", "LAX - Los Angeles, CA", time, 350, 550.0);
            flightRepository.save(flight1);

            time = LocalTime.of(12, 30, 00);
            Flight flight2 = new Flight("102", "IAD - Washington, DC - Dulles", "LAX - Los Angeles, CA", time, 345, 550.0);
            flightRepository.save(flight2);

            time = LocalTime.of(16, 50, 00);
            Flight flight3 = new Flight("103", "IAD - Washington, DC - Dulles", "LAX - Los Angeles, CA", time, 355, 550.0);
            flightRepository.save(flight3);

            time = LocalTime.of(9, 30, 00);
            Flight flight4 = new Flight("201", "LAX - Los Angeles, CA", "IAD - Washington, DC - Dulles", time, 325, 550.0);
            flightRepository.save(flight4);
            time = LocalTime.of(13, 25, 00);
            Flight flight5 = new Flight("202", "LAX - Los Angeles, CA", "IAD - Washington, DC - Dulles", time, 330, 350.0);
            flightRepository.save(flight5);

            time = LocalTime.of(17, 40, 00);
            Flight flight6 = new Flight("203", "LAX - Los Angeles, CA", "IAD - Washington, DC - Dulles", time, 320, 350.0);
            flightRepository.save(flight6);

            time = LocalTime.of(8, 55, 00);
            Flight flight7 = new Flight("301", "ORD - Chicago, IL - O'Hare", "DFW - Dallas/Fort Worth, TX", time, 140, 175.0);
            flightRepository.save(flight7);

            time = LocalTime.of(11, 35, 00);
            Flight flight8 = new Flight("401", "DFW - Dallas/Fort Worth, TX", "ORD - Chicago, IL - O'Hare", time, 140, 175.0);
            flightRepository.save(flight8);

            time = LocalTime.of(7, 30, 00);
            Flight flight9 = new Flight("500", "IAD - Washington, DC - Dulles", "ORD - Chicago, IL - O'Hare", time, 115, 280.0);
            flightRepository.save(flight9);

            time = LocalTime.of(9, 30, 00);
            Flight flight10 = new Flight("510", "IAD - Washington, DC - Dulles", "ORD - Chicago, IL - O'Hare", time, 115, 280.0);
            flightRepository.save(flight10);

            time = LocalTime.of(17, 30, 00);
            Flight flight11 = new Flight("550", "IAD - Washington, DC - Dulles", "MIA - Miami, FL - International", time, 170, 380.0);
            flightRepository.save(flight11);

            time = LocalTime.of(6, 10, 00);
            Flight flight12 = new Flight("505", "ORD - Chicago, IL - O'Hare", "IAD - Washington, DC - Dulles", time, 115, 280.0);
            flightRepository.save(flight12);

            time = LocalTime.of(8, 15, 00);
            Flight flight13 = new Flight("516", "ORD - Chicago, IL - O'Hare", "IAD - Washington, DC - Dulles", time, 115, 280.0);
            flightRepository.save(flight13);

            time = LocalTime.of(4, 50, 00);
            Flight flight14 = new Flight("555", "MIA - Miami, FL - International", "IAD - Washington, DC - Dulles", time, 170, 380.0);
            flightRepository.save(flight14);

            time = LocalTime.of(11, 30, 00);
            Flight flight15 = new Flight("623", "IAD - Washington, DC - Dulles", "DFW - Dallas/Fort Worth, TX", time, 205, 300.0);
            flightRepository.save(flight15);

            time = LocalTime.of(14, 10, 00);
            Flight flight16 = new Flight("668", "DFW - Dallas/Fort Worth, TX", "IAD - Washington, DC - Dulles", time, 205, 300.0);
            flightRepository.save(flight16);

            time = LocalTime.of(15, 40, 00);
            Flight flight17 = new Flight("690", "IAD - Washington, DC - Dulles", "SFO - San Francisco, CA", time, 355, 690.0);
            flightRepository.save(flight17);

            time = LocalTime.of(10, 15, 00);
            Flight flight18 = new Flight("693", "SFO - San Francisco, CA", "IAD - Washington, DC - Dulles", time, 300, 690.0);
            flightRepository.save(flight18);

            Passenger pass1 = new Passenger(user1.getFirstName(), user1.getLastName(), "13D");
            Set<Passenger> set1 = new HashSet<>();
            set1.add(pass1);

            Passenger pass2 = new Passenger(user2.getFirstName(), user2.getLastName(), "6B");
            Set<Passenger> set2 = new HashSet<>();
            set2.add(pass2);

            Passenger pass3 = new Passenger(user3.getFirstName(), user3.getLastName(), "16A");
            Passenger pass4 = new Passenger(user5.getFirstName(), user5.getLastName(), "16B");
            Passenger pass5 = new Passenger(user6.getFirstName(), user6.getLastName(), "16C");
            Set<Passenger> set3 = new HashSet<>();
            set3.add(pass3);
            set3.add(pass4);
            set3.add(pass5);

            Date departDate = new Date();
            Date returnDate = new Date();
            DateFormat dsdf = new SimpleDateFormat("dd-MM-yyyy");

            String dDate1 = "13-10-2019";
            String rDate1 = "18-10-2019";
            departDate = dsdf.parse(dDate1);
            returnDate = dsdf.parse(rDate1);
            Reservation rsvr1 = new Reservation(departDate, returnDate, "Economy", 1, flight1, flight1.getBasePrice(), flight4, flight4.getBasePrice(), set1, user1);
            reservationRepository.save(rsvr1);
            pass1.setReservation(rsvr1);
            passengerRepository.save(pass1);

            String dDate2 = "20-10-2019";
            String rDate2 = "28-10-2019";
            departDate = dsdf.parse(dDate2);
            returnDate = dsdf.parse(rDate2);
            Reservation rsvr2 = new Reservation(departDate, returnDate, "Business", 3, flight3, flight3.getBasePrice() * 3, null, 0, set3, user);
            reservationRepository.save(rsvr2);

            pass3.setReservation(rsvr2);
            pass4.setReservation(rsvr2);
            pass5.setReservation(rsvr2);
            passengerRepository.save(pass3);
            passengerRepository.save(pass4);
            passengerRepository.save(pass5);

        }


    }
}
