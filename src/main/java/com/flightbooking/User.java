package com.flightbooking;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flightbooking.database.Data;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class User {
    @JsonProperty("id")
    final String userId;
    @JsonProperty("name")
    final String name;
    @JsonProperty("surname")
    final String surname;
    @JsonProperty("booked tickets")
    List<String> bookedTickets;

    @JsonCreator
    public User(
            @JsonProperty("id") String userId,
            @JsonProperty("name") String name,
            @JsonProperty("surname") String surname,
            @JsonProperty("booked tickets") List<String> bookedTickets) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.bookedTickets = bookedTickets;
    }

    public User() {
        this.userId = "";
        this.name = "";
        this.surname = "";
        this.bookedTickets = new ArrayList<>();
    }

    public User(String userId, String name, String surname) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.bookedTickets = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public List<String> getBookedTickets() {
        return bookedTickets;
    }
    public void setBookedTickets(List<String> bookedTickets) {
        this.bookedTickets = bookedTickets;
    }
    public static User createNewUser(String name, String surname, Data database){
        String randomid = UUID.randomUUID().toString();
        User newUser = new User(name, surname, randomid);
        newUser.setBookedTickets(new ArrayList<>());
        database.addUser(newUser);
        return newUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", bookedTickets=" + bookedTickets +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(bookedTickets, user.bookedTickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, surname, bookedTickets);
    }
}
