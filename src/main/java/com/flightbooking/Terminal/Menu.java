package com.flightbooking.Terminal;

import com.flightbooking.Flight;
import com.flightbooking.Ticket;
import com.flightbooking.User;
import com.flightbooking.database.Data;
import com.flightbooking.SearchEngine.Search;
import com.flightbooking.Booking.BookTickets;

import java.util.List;
import java.util.Scanner;
import java.util.*;

public class Menu {

    private final BookTickets bookTickets;
    private final Search searchEngine;
    private final Scanner scanner;
    private final Data database;

    public Menu(BookTickets bookTickets, Search searchEngine,Data database) {
        this.bookTickets = bookTickets;
        this.searchEngine = searchEngine;
        this.scanner = new Scanner(System.in);
        this.database = database;
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n----- Main Menu -----");
            System.out.println("1. View all flights");
            System.out.println("2. Book a ticket");
            System.out.println("3. Cancel a ticket");
            System.out.println("4. View my bookings");
            System.out.println("5. Exit");

            System.out.print("Choose an option: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        viewAllFlights();
                        break;
                    case 2:
                        bookTicket();
                        break;
                    case 3:
                        cancelTicket();
                        break;
                    case 4:
                        viewMyBookings();
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input,Please enter a number");
            }
        }
    }




    private void viewAllFlights() {
         /// display all available flights!!!!!
        List<Flight> flights = database.getFlights();


        if (flights.isEmpty()) {
            System.out.println("No flights available.");
            return;
        }

        System.out.println("\nAll Flights:");
        for (int i = 0; i < flights.size(); i++) {
            Flight flight = flights.get(i);
            System.out.printf("%d. %s - %s - %s (Seats: %d)%n",
                    i + 1,
                    flight.getFlightId(),
                    flight.getDestination(),
                    flight.getDateAndTime(),
                    flight.getAvailableSeats());
        }

        System.out.print("\nSelect a flight to view details (enter number, or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;

            if (choice < 1 || choice > flights.size()) {
                System.out.println("Invalid flight selection.");
                return;
            }

            Flight selectedFlight = flights.get(choice - 1);
            displayFlightDetails(selectedFlight);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }


    private void displayFlightDetails(Flight flight) {
        System.out.println("\nFlight Details:");
        System.out.println("Flight ID: " + flight.getFlightId());
        System.out.println("Destination: " + flight.getDestination());
        System.out.println("Date and Time: " + flight.getDateAndTime());
        System.out.println("Available Seats: " + flight.getAvailableSeats());
        System.out.println("Passengers: " + flight.getPassengers().size() + " people booked");


    }

private void bookTicket() {
    Flight flight = selectFlight();
    if (flight == null) {
        System.out.println("No flight selected.");
        return;
    }

    System.out.println("\nEnter your name: ");
    String name = scanner.nextLine();
    System.out.println("Enter your surname: ");
    String surname = scanner.nextLine();

    User user = searchEngine.filterUser(name, surname);
    if (user == null) {
        System.out.println("User not found. Create an account? (Y/N)");
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("Y")) {
            user = new User(UUID.randomUUID().toString(), name, surname);
            database.addUser(user);
            //database.saveToFile();
        } else {
            return;
        }
    }

    List<User> passengers = new ArrayList<>();
    /*
    if (flight.getAvailableSeats() < passengers.size()) {
        System.out.println("Not enough seats available.");
        return;
    }*/
    passengers.add(user);
    //bookTickets.Booking(flight, user, passengers);
    bookTickets.Booking(flight, user, passengers);
    System.out.println("Booking successful!");
}


    private void cancelTicket() {
            System.out.println("\nChoose how to cancel the ticket:");
            System.out.println("1. By Ticket ID");
            System.out.println("2. By Name and Surname");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("\nEnter your ticket ID to cancel: ");
                String ticketId = scanner.nextLine();
                bookTickets.cancelTickets(ticketId);
            } else if (choice.equals("2")) {
                System.out.print("\nEnter your name: ");
                String name = scanner.nextLine();
                System.out.print("Enter your surname: ");
                String surname = scanner.nextLine();
                bookTickets.cancelTickets(name, surname);
            } else {
                System.out.println("Invalid choice, please try again.");
            }
        }



    private Flight selectFlight() {
        System.out.print("\nEnter destination: ");
        String destination = scanner.nextLine();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter number of passengers: ");
        try {
            int quantity = Integer.parseInt(scanner.nextLine());
            List<Flight> flights = searchEngine.filterFlights(destination, date, quantity);

            if (flights.isEmpty()) {
                System.out.println("No flights available.");
                return null;
            }

            System.out.println("\nAvailable flights:");
            for (int i = 0; i < flights.size(); i++) {
                Flight flight = flights.get(i);
                System.out.printf("%d. %s - %s - %s (Seats: %d)%n",
                        i + 1,
                        flight.getFlightId(),
                        flight.getDestination(),
                        flight.getDateAndTime(),
                        flight.getAvailableSeats());
            }

            System.out.print("\nSelect a flight (enter number, or 0 to cancel): ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return null;

            if (choice < 1 || choice > flights.size()) {
                System.out.println("Invalid selection.");
                return null;
            }

            return flights.get(choice - 1);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return null;
        }
    }
    private void viewMyBookings() {
        System.out.print("\nEnter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your surname: ");
        String surname = scanner.nextLine();

        User user = searchEngine.filterUser(name, surname);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }



        List<Ticket> userTickets = searchEngine.filterTickets(name, surname);
        if (userTickets.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("\nYour Bookings:");
        for (Ticket ticket : userTickets) {
            Flight flight = searchEngine.findFlight(ticket.getFlightId());
            System.out.println("Flight ID: " + flight.getFlightId());
            System.out.println("Destination: " + flight.getDestination());
            System.out.println("Date: " + flight.getDateAndTime());
            System.out.println("-----------------------");
        }
    }
}
