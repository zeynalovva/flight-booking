package com.flightbooking;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flightbooking.Database.Data;

import java.util.List;
import java.util.Objects;

public class Ticket {
    @JsonProperty("id")
    final String ticketId;
    @JsonProperty("flight id")
    final String flightId;
    @JsonProperty("user id")
    final String userId;
    @JsonProperty("booker id")
    final String bookerId;

    @JsonCreator
    public Ticket(
            @JsonProperty("id") String ticketId,
            @JsonProperty("flight id") String flightId,
            @JsonProperty("user id") String userId,
            @JsonProperty("booker id") String bookerId) {
        this.ticketId = ticketId;
        this.flightId = flightId;
        this.userId = userId;
        this.bookerId = bookerId;
    }

    public Ticket() {
        this.ticketId = "";
        this.flightId = "";
        this.userId = "";
        this.bookerId = "";
    }

    public String getTicketId() {
        return ticketId;
    }
    public String getFlightId() {
        return flightId;
    }
    public String getUserId() {
        return userId;
    }
    public String getBookerId() {
        return bookerId;
    }
    public static Ticket createTicket(Flight flight, User booker, User passenger, Data database){
        String randomid= randomizer(database.getTickets());
        Ticket newTicket = new Ticket(randomid, flight.getFlightId(), passenger.getUserId(), booker.getUserId());
        database.addTicket(newTicket);
        return newTicket;
    }
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", flightId='" + flightId + '\'' +
                ", userId='" + userId + '\'' +
                ", bookerId='" + bookerId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket ticket)) return false;
        return Objects.equals(ticketId, ticket.ticketId) &&
                Objects.equals(flightId, ticket.flightId) &&
                Objects.equals(userId, ticket.userId) &&
                Objects.equals(bookerId, ticket.bookerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, flightId, userId, bookerId);
    }

    public static String randomizer(List<Ticket> list){
        int counter;
        Ticket last;
        try{
            last = list.getLast();
        }
        catch (Exception e){
            last = null;
        }
        if(last == null)
            counter = 0;
        else{
            String ID = last.getTicketId();
            int temp = Integer.parseInt(ID.substring(3));
            counter = temp;
        }
        counter++;
        return String.valueOf("TCK" + counter);
    }

}
