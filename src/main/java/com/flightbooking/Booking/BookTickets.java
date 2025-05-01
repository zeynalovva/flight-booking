package com.flightbooking.Booking;

import com.flightbooking.Flight;
import com.flightbooking.Ticket;
import com.flightbooking.User;
import com.flightbooking.Database.Data;
import com.flightbooking.SearchEngine.Search;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookTickets {
    private Data database;
    private final Scanner scanner;
    private final Search searchEngine;

    public BookTickets(Search search) {
        this.scanner = new Scanner(System.in);
        this.searchEngine = search ;
        this.database = search.data;
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
            Ticket ticket = Ticket.createTicket(flight, booker, passenger, database);
            newTickets.add(ticket);

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
            clearChat();
            System.out.println("Ticket with the ID of " + ID + " is canceled!");
        }
        else {
            clearChat();
            System.out.println("Ticket could not be found!");
        }
    }



    //enter booker credentials
    public User enterBookerCredentials() {
        System.out.println("\n:   Booker Information   :");
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Surname: ");
        String surname = scanner.nextLine().trim();

        return database.getUsers().stream()
                .filter(u -> u.getName().equalsIgnoreCase(name)
                        && u.getSurname().equalsIgnoreCase(surname))
                .findFirst()
                .orElseGet(() -> User.createNewUser(name, surname, database));
    }

    //add passenger information
    public List<User> createPassengers(int quantity) {
        List<User> passengers = new ArrayList<>();
        while (quantity > 0) {
            System.out.println("\n:    Add Passenger Information    :");
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty!");
                continue;
            }

            System.out.print("Surname: ");
            String surname = scanner.nextLine().trim();

            User passenger = database.getUsers().stream()
                    .filter(u -> u.getName().equalsIgnoreCase(name)
                            && u.getSurname().equalsIgnoreCase(surname))
                    .findFirst()
                    .orElseGet(() -> User.createNewUser(name, surname, database));

            passengers.add(passenger);
            quantity--;
        }
        return passengers;

    }

    private void clearChat(){
        for (int i = 0; i < 80; ++i) {
            System.out.println();
        }
    }

}
