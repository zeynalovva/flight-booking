package com.flightbooking.murad;

import com.flightbooking.Flight;
import com.flightbooking.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class BookTickets {
    private Database database;
    private final Scanner scanner;

    public BookTickets(Database database) {
        this.database = database;
        this.scanner = new Scanner(System.in);
    }
    public void StartBookingTickets(String bookerId, String flightId, List<String> passengerIds) {
        try {
            List<Flight> filteredFlights = filterFlights();
            displayFlights(filteredFlights);
            Flight selectedFlight = selectFlight(filteredFlights);
            if (selectedFlight == null) return;//display the Filtered flights
            if (selectedFlight.getAvailableSeats() > 0) {
                enterBookerCredentials();
                System.out.println("Booking tickets...");
                database.bookTickets(bookerId, selectedFlight.getFlightId(), passengerIds);
                System.out.println("Tickets booked successfully!");
            } else {
                System.out.println("No available seats on this flight.");
            }

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
    private void enterBookerCredentials() {
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


}
