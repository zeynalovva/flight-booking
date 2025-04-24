package com.flightbooking;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.ArrayList;
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

}
