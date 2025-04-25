package com.flightbooking.murad;

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
            System.out.print("Enter destination: ");
            String destination = scanner.nextLine();
            System.out.print("Enter travel date (dd/MM/yyyy): ");
            String date = scanner.nextLine();
            System.out.print("Number of seats needed: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();
            List<Flight> filteredFlights = searchEngine.filterFlights(destination, date, quantity);
            if(filteredFlights.isEmpty()) {
                System.out.println("No flights available matching your criteria");
                return;
            }
            displayFlights(filteredFlights);//display the Filtered flights
            Flight selectedFlight = selectFlight(filteredFlights);
            if (selectedFlight == null) return;
            User booker = enterBookerCredentials();
            List<User> passengers = addPassengerInformation();


        } catch (Exception e) {
            System.out.printf("Error: " + e.getMessage());;
        }
    }


    public void cancelTickets(String TicketId){}

    public void addPessengers(String name, String surname, String passportId) {
    }
    private void displayFlights(List<Flight> flights) {
        System.out.println("\nAvailable Flights:");
        for (int i = 0; i < flights.size(); i++) {
            Flight f = flights.get(i);
            System.out.printf("%d. %s | %s | Seats: %d | Date: %s\n",
                    i + 1, f.getFlightId(), f.getDestination(),
                    f.getAvailableSeats(), f.getDateAndTime());
        }
    }
    //select flight by its ID
    private Flight selectFlight(List<Flight> flights) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the flight ID to book: ");
        String flightId = scanner.nextLine();
        for (Flight f : flights) {
            if (f.getFlightId().equals(flightId)) {
                return f;
            }
        }
        System.out.println("Invalid flight ID.");
        return null;
    }

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
