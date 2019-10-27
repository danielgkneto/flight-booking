package com.danielgkneto.mcjavabc.flightbooking.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "departure_date", nullable = false)
    private Date departureDate;

    @Column(name = "return_date")
    private Date returnDate;

    @Column(name = "flight_class", nullable = false)
    private String flightClass;

    @Column(name = "number_passengers", nullable = false)
    @Min(1)
    private int numberPassengers;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departure_flight_id", nullable = false)
    private Flight departureFlight;

    @Column(name = "departure_price")
    private double departurePrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "return_flight_id")
    private Flight returnFlight;

    @Column(name = "return_price")
    private double returnPrice;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Passenger> passengers = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Reservation() {

    }

    public Reservation(Date departureDate, Date returnDate, String flightClass, @Min(1) int numberPassengers, Flight departureFlight, double departurePrice, Flight returnFlight, double returnPrice, Set<Passenger> passengers, User user) {
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.flightClass = flightClass;
        this.numberPassengers = numberPassengers;
        this.departureFlight = departureFlight;
        this.departurePrice = departurePrice;
        this.returnFlight = returnFlight;
        this.returnPrice = returnPrice;
        this.passengers = passengers;
        this.user = user;
    }

    public double calcPrice(double basePrice){
        double pricePerPassenger;
        if (this.getFlightClass().equalsIgnoreCase("economy")){
            pricePerPassenger = basePrice;
        }else if (this.getFlightClass().equalsIgnoreCase("business")){
            pricePerPassenger = 2 * basePrice;
        }else{
            pricePerPassenger = 3 * basePrice;
        }
        return (this.numberPassengers * pricePerPassenger);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(String flightClass) {
        this.flightClass = flightClass;
    }

    public int getNumberPassengers() {
        return numberPassengers;
    }

    public void setNumberPassengers(int numberPassengers) {
        this.numberPassengers = numberPassengers;
    }

    public Flight getDepartureFlight() {
        return departureFlight;
    }

    public void setDepartureFlight(Flight departureFlight) {
        this.departureFlight = departureFlight;
    }

    public double getDeparturePrice() {
        return departurePrice;
    }

    public void setDeparturePrice(double departurePrice) {
        this.departurePrice = departurePrice;
    }

    public Flight getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(Flight returnFlight) {
        this.returnFlight = returnFlight;
    }

    public double getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(double returnPrice) {
        this.returnPrice = returnPrice;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
