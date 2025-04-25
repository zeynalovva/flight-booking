package com.flightbooking.Booking;

import com.flightbooking.Flight;
import com.flightbooking.Ticket;
import com.flightbooking.User;
import com.flightbooking.database.Data;
import com.flightbooking.SearchEngine.Search;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class BookTickets {
    private Data database;
    private final Scanner scanner;
    private final Search searchEngine;


    public BookTickets(Data database) {
        this.database = database;
        this.scanner = new Scanner(System.in);
        this.searchEngine = new Search(database);
    }
    public void StartBookingTickets(String bookerId, String flightId, List<String> passengerIds) {
        try {
            User booker = enterBookerCredentials();
            List<User> passengers = addPassengerInformation();
            database.saveToFile();

        } catch (Exception e) {
            System.out.printf("Error: " + e.getMessage());;
        }
    }

    //process booking
    public void Booking(Flight flight, User booker, List<User> passengers){
        List<Ticket> newTickets = new ArrayList<>();
        for(User passenger : passengers) {
            Ticket ticket = createTicket(flight, booker, passenger);
            newTickets.add(ticket);
            database.addTicket(ticket);

            // Update flight passengers
            flight.getPassengers().add(passenger.getUserId());
        }

        // Update flight seats
        flight.setAvailableSeats(flight.getAvailableSeats() - passengers.size());
        Flight tempFlight=flight;
        database.getFlights().removeIf(f -> f.getFlightId().equals(tempFlight.getFlightId()));
        database.addFlight(tempFlight);

        User tempBooker = new User(
                booker.getUserId(),
                booker.getName(),
                booker.getSurname(),
                new ArrayList<>(booker.getBookedTickets())
        );
        newTickets.forEach(ticket -> tempBooker.getBookedTickets().add(ticket.getTicketId()));

        database.getUsers().removeIf(u -> u.getUserId().equals(tempBooker.getUserId()));
        database.addUser(tempBooker);

        // Add all new tickets
        newTickets.forEach(database::addTicket);

    }



    public void cancelTickets(String TicketId){}


    private User createNewUser(String name, String surname) {
        String randomid=UUID.randomUUID().toString();
        User newUser = new User(name, surname, randomid);
        newUser.setBookedTickets(new ArrayList<>());
        database.addUser(newUser);
        return newUser;
    }

    //enter booker credentials
    private User enterBookerCredentials() {
        System.out.println("\n=== Booker Information ===");
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Surname: ");
        String surname = scanner.nextLine().trim();

        return database.getUsers().stream()
                .filter(u -> u.getName().equalsIgnoreCase(name)
                        && u.getSurname().equalsIgnoreCase(surname))
                .findFirst()
                .orElseGet(() -> createNewUser(name, surname));
    }

    //add passenger information
    private List<User> addPassengerInformation() {
        List<User> passengers = new ArrayList<>();
        while (true) {
            System.out.println("\n    Add Passenger Information    ");
            System.out.print("Name (empty to finish): ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) break;

            System.out.print("Surname: ");
            String surname = scanner.nextLine().trim();

            User passenger = database.getUsers().stream()
                    .filter(u -> u.getName().equalsIgnoreCase(name)
                            && u.getSurname().equalsIgnoreCase(surname))
                    .findFirst()
                    .orElseGet(() -> createNewUser(name, surname));

            passengers.add(passenger);
        }
        return passengers;

    }

    private Ticket createTicket(Flight flight, User booker, User passenger) {
        String randomid=UUID.randomUUID().toString();
        Ticket newTicket = new Ticket(randomid, flight.getFlightId(), passenger.getUserId(), booker.getUserId());
        return newTicket;
    }



}
