package com.flightbooking;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;


public class Flight {
    @JsonProperty("id")
    final String flightId;
    @JsonProperty("destination")
    final String destination;
    @JsonProperty("date and time")
    final String dateAndTime;
    @JsonProperty("passengers")
    List<String> passengers;
    @JsonProperty("free seats")
    int availableSeats;

    @JsonCreator
    public Flight(@JsonProperty("id") String flightId,
                  @JsonProperty("destination") String destination,
                  @JsonProperty("date and time") String dateAndTime,
                  @JsonProperty("passengers") List<String> passengers,
                  @JsonProperty("free seats") int availableSeats) {
        this.flightId = flightId;
        this.destination = destination;
        this.dateAndTime = dateAndTime;
        this.passengers = passengers;
        this.availableSeats = availableSeats;
    }


    public Flight(){
        this.flightId = "";
        this.destination = "";
        this.dateAndTime = "";
        this.passengers = new ArrayList<>();
        this.availableSeats = 0;
    }

    public Flight(String flightId, String destination, String dateAndTime) {
        this.flightId = flightId;
        this.destination = destination;
        this.dateAndTime = dateAndTime;
        this.passengers = new ArrayList<>();
    }


    public String getFlightId() {
        return flightId;
    }
    public String getDestination() {
        return destination;
    }
    public String getDateAndTime() {
        return dateAndTime;
    }
    public List<String> getPassengers() {
        return passengers;
    }
    public int getAvailableSeats() {
        return availableSeats;
    }
    public void setPassengers(List<String> passengers) {
        this.passengers = passengers;
    }
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }


    @Override
    public String toString() {
        return "Flight{" +
                "flightId='" + flightId + '\'' +
                ", destination='" + destination + '\'' +
                ", dateAndTime='" + dateAndTime + '\'' +
                ", passengers=" + passengers +
                ", availableSeats=" + availableSeats +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flight flight)) return false;
        return availableSeats == flight.availableSeats &&
                Objects.equals(flightId, flight.flightId) &&
                Objects.equals(destination, flight.destination) &&
                Objects.equals(dateAndTime, flight.dateAndTime) &&
                Objects.equals(passengers, flight.passengers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId, destination, dateAndTime, passengers, availableSeats);
    }
}
