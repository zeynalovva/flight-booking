package com.flightbooking.Database;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.flightbooking.Flight;
import com.flightbooking.User;
import com.flightbooking.Ticket;


/**
 * The <code>Data</code> class is responsible for managing and storing flight, user, and ticket data.
 * It implements the `DataManager` interface to provide functionality for loading/saving data from/to database.
 */
public class Data implements DataManager{
    private static final String FILE_PATH = "src/main/resources/data.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @JsonProperty
    private List<Flight> flights;
    @JsonProperty
    private List<User> users;
    @JsonProperty
    private List<Ticket> tickets;

    public Data() {
        this.flights = new ArrayList<>();
        this.users = new ArrayList<>();
        this.tickets = new ArrayList<>();
    }

    public List<Flight> getFlights() {
        return flights;
    }
    public List<User> getUsers() {
        return users;
    }
    public List<Ticket> getTickets() {
        return tickets;
    }
    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
    public void setUsers(List<User> users) {
        this.users = users;
    }
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    /**
     * Add a new flight to the list of flights in 'Data' object.
     * It checks if the flight is not null and not already present in the list.
     * @param flight The flight to be added.
     * @return `true` if the flight was added successfully, `false` otherwise.
     */
    public boolean addFlight(Flight flight) {
        if (flight != null && !this.flights.contains(flight)) {
            return this.flights.add(flight);
        }
        return false;
    }

    /**
     * Add a new user to the list of users in 'Data' object.
     * It checks if the user is not null and not already present in the list.
     * @param user The user to be added.
     * @return `true` if the user was added successfully, `false` otherwise.
     */
    public boolean addUser(User user) {
        if (user != null && !this.users.contains(user)) {
            return this.users.add(user);
        }
        return false;
    }

    /**
     * Add a new ticket to the list of tickets in 'Data' object.
     * It checks if the ticket is not null and not already present in the list.
     * @param ticket The ticket to be added.
     * @return `true` if the ticket was added successfully, `false` otherwise.
     */
    public boolean addTicket(Ticket ticket) {
        if (ticket != null && !this.tickets.contains(ticket)) {
            return this.tickets.add(ticket);
        }
        return false;
    }



    /**
     * Load all data from JSON file.
     * After loading, the data is stored in the `Data` object.
     * You can access the loaded data using the getter methods.
     * @return A `Data` object which contains the loaded data.
     */
    public static Data loadFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists() || file.length() == 0) {
                return new Data();
            }
            return OBJECT_MAPPER.readValue(file, Data.class);
        } catch (IOException e) {
            return new Data();
        }
    }

    /**
     * Save all data to JSON file.
     * This method serializes the `Data` object and writes it to the specified file.
     * You can call this method to persist any changes made to the data.
     */
    public void saveToFile() {
        try {
            OBJECT_MAPPER.writeValue(new File(FILE_PATH), this);
        } catch (IOException ignored) {
        }
    }
}
