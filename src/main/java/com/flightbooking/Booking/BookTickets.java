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

    public BookTickets(Data database, Search search) {
        this.database = database;
        this.scanner = new Scanner(System.in);
        this.searchEngine = search ;
    }

    /**
     *
     * @param flight the flight that the booker has chosen
     * @param booker the user himself
     * @param passengers list of passengers who are going to be in the flight
     */

    public void Booking(Flight flight, User booker, List<User> passengers){
        List<Ticket> newTickets = new ArrayList<>();
        for(User passenger : passengers) {
            Ticket ticket = Ticket.createTicket(flight, booker, passenger);
            newTickets.add(ticket);
            database.addTicket(ticket);

            // Update flight passengers
            flight.getPassengers().add(passenger.getUserId());
        }

        // Update flight seats
        flight.setAvailableSeats(flight.getAvailableSeats() - passengers.size());

        // Add the tickets to the bookers list
        newTickets.forEach(x -> booker.getBookedTickets().add(x.getTicketId()));
    }

    public void cancelTickets(String ID){
        Ticket temp = searchEngine.findTicket(ID);
        if(temp != null){
            // Removes the ticket ID from the booker's list
            String bookerID = temp.getBookerId();
            User booker = searchEngine.findUser(bookerID);
            booker.getBookedTickets().remove(temp.getTicketId());

            // Removes the user ID from the flight's passenger list and increases the available seats
            String flightID = temp.getFlightId();
            Flight flight = searchEngine.findFlight(flightID);
            flight.getPassengers().remove(temp.getUserId());
            flight.setAvailableSeats(flight.getAvailableSeats() + 1);

            database.getTickets().remove(temp);
        }
        else System.out.println("Ticket could not be found!");
    }


    public void cancelTickets(String name, String surname) {
        List<Ticket> tickets = searchEngine.filterTickets(name, surname);
        if (!tickets.isEmpty()) {
            for (Ticket ticket : tickets) {
                // Update flight
                Flight flight = searchEngine.findFlight(ticket.getFlightId());
                if (flight != null) {
                    Flight tempFlight = new Flight(
                            flight.getFlightId(),
                            flight.getDestination(),
                            flight.getDateAndTime(),
                            new ArrayList<>(flight.getPassengers()),
                            flight.getAvailableSeats() + 1  // Fixed from -1 to +1
                    );
                    tempFlight.getPassengers().remove(ticket.getUserId());
                    database.getFlights().remove(flight);
                    database.addFlight(tempFlight);
                }
                User booker = null;
                for (User user : database.getUsers()) {
                    if (user.getUserId().equals(ticket.getBookerId())) {
                        booker = user;
                        break;
                    }
                }
                if (booker != null) {
                    User tempBooker = new User(
                            booker.getUserId(),
                            booker.getName(),
                            booker.getSurname(),
                            new ArrayList<>(booker.getBookedTickets())
                    );
                    tempBooker.getBookedTickets().remove(ticket.getTicketId());
                    database.getUsers().remove(booker);
                    database.addUser(tempBooker);
                }
                database.getTickets().remove(ticket);
            }
        }
    }




    //enter booker credentials
    //private User enterBookerCredentials() {
    //    System.out.println("\n:   Booker Information   :");
    //    System.out.print("Name: ");
    //    String name = scanner.nextLine().trim();
    //    System.out.print("Surname: ");
    //    String surname = scanner.nextLine().trim();
    //
    //    return database.getUsers().stream()
    //            .filter(u -> u.getName().equalsIgnoreCase(name)
    //                    && u.getSurname().equalsIgnoreCase(surname))
    //            .findFirst()
    //            .orElseGet(() -> User.createNewUser(name, surname, database));
    //}

    //add passenger information
    private List<User> createPassengers() {
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
                    .orElseGet(() -> User.createNewUser(name, surname, database));

            passengers.add(passenger);
        }
        return passengers;

    }


}
