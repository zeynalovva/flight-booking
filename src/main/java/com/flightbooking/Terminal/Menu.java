package com.flightbooking.Terminal;

import com.flightbooking.Flight;
import com.flightbooking.Ticket;
import com.flightbooking.User;
import com.flightbooking.Database.Data;
import com.flightbooking.SearchEngine.Search;
import com.flightbooking.Booking.BookTickets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final BookTickets bookTickets;
    private final Search searchEngine;
    private final Scanner scanner;
    private final Data database;
    private int quantity;
    public Menu(BookTickets bookTickets, Search searchEngine) {
        this.bookTickets = bookTickets;
        this.searchEngine = searchEngine;
        this.scanner = new Scanner(System.in);
        this.database = searchEngine.data;
    }

    public void showMainMenu() {
        while (true) {
            List<Flight> flights = database.getFlights();
            showAllFlights(flights);
            System.out.println("\n|----- Main Menu -----|");
            System.out.println("1. View a flight");
            System.out.println("2. Book a ticket");
            System.out.println("3. Cancel a ticket");
            System.out.println("4. View my bookings");
            System.out.println("5. Exit");

            System.out.print("Choose an option: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        viewSingleFlight(flights);
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
                        clearChat();
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                clearChat();
                System.out.println("Invalid input,Please enter a number");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showSingleFlight(int index, Flight flight, int menu){
        if(menu == 1){
            String[] temp = flight.getDateAndTime().split(" ");
            System.out.printf("%d. Kiev -> %s | %s | %s|\n",
                    index+1,
                    flight.getDestination(),
                    temp[0],
                    temp[1]);
        }
        else{
            String[] temp = flight.getDateAndTime().split(" ");
            System.out.printf("%s | Kiev -> %s | %s | %s | (Seats: %d)%n",
                    flight.getFlightId(),
                    flight.getDestination(),
                    temp[0],
                    temp[1],
                    flight.getAvailableSeats());
        }
    }

    private void showAllFlights(List<Flight> flights){
        if (flights.isEmpty()) {
            System.out.println("No flights available.");
            return;
        }
        int counter = 0;
        System.out.println("\n|----- All Flights -----|");
        for (int i = 0;i<flights.size();i++) {
            if(isWithinNext24Hours(flights.get(i).getDateAndTime())){
                showSingleFlight(counter, flights.get(i), 1);
                counter++;
            }
        }
    }


    private void viewSingleFlight(List<Flight> flights) {
        clearChat();
        showAllFlights(flights);
        System.out.print("\nSelect a flight to view details (enter serial number, flight ID, or 0 to cancel): ");
        try {
            String input = scanner.nextLine();
            if(input.matches("\\d+")){
                int choice = Integer.parseInt(input);
                clearChat();
                if (choice == 0) return;

                if (choice < 1 || choice > flights.size()) {

                    System.out.println("Invalid flight selection.");
                    return;
                }
                Flight selectedFlight = flights.get(choice - 1);
                showSingleFlight(0, selectedFlight, 0);
            }
            else{
                Flight selectedFlight = searchEngine.findFlight(input);
                clearChat();
                if(selectedFlight == null){
                    System.out.println("Invalid flight selection.");
                    return;
                }
                showSingleFlight(0, selectedFlight, 0);
            }
        } catch (NumberFormatException e) {
            clearChat();
            System.out.println("Invalid input.");
        }
    }

    private void bookTicket() {
        clearChat();
        Flight flight = selectFlight();
        if(flight == null) return;
        User booker = bookTickets.enterBookerCredentials();
        List<User> passenger = bookTickets.createPassengers(quantity);
        bookTickets.Booking(flight, booker, passenger);
        database.saveToFile();
    }



    private void cancelTicket() {
        System.out.print("\nEnter your ticket ID to cancel: ");
        String ticketId = scanner.nextLine();
        bookTickets.cancelTickets(ticketId);
        database.saveToFile();
    }



    private Flight selectFlight() {
        System.out.print("\nEnter destination: ");
        String destination = scanner.nextLine();
        if(destination.isEmpty()){
            System.out.println("Invalid input.");
            return null;
        }
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter number of passengers: ");
        try {
            quantity = Integer.parseInt(scanner.nextLine());
            List<Flight> flights;
            if(date.isEmpty())
                flights = searchEngine.filterFlights(destination, quantity);
            else
                flights = searchEngine.filterFlights(destination, date, quantity);

            if (flights.isEmpty()) {
                System.out.println("No flights available.");
                return null;
            }
            int counter = 0;
            System.out.println("\nAvailable flights:");
            for (int i = 0; i < flights.size(); i++) {
                Flight flight = flights.get(i);
                if(!isBefore(flight.getDateAndTime())){
                    showSingleFlight(counter, flight, 0);
                    counter++;
                }
            }

            System.out.print("\nSelect a flight (enter the flight ID): ");
            String choice = scanner.nextLine();
            clearChat();
            if (choice.isEmpty()) return null;
            Flight temp = searchEngine.findFlight(choice);
            if(temp == null){
                System.out.println("Invalid input.");
                return null;
            }
            return temp;
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
        clearChat();
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
            System.out.println("Ticket ID: " + ticket.getTicketId());
            System.out.println("Flight ID: " + flight.getFlightId());
            System.out.println("Destination: " + flight.getDestination());
            System.out.println("Date: " + flight.getDateAndTime());
            System.out.println("-----------------------");
        }
    }
    private void clearChat(){
        for (int i = 0; i < 80; ++i) {
            System.out.println();
        }
    }

    public static boolean isWithinNext24Hours(String dateString) {
        String formatPattern = "yyyy-MM-dd HH:mm";
        LocalDateTime targetDateTime = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);

            targetDateTime = LocalDateTime.parse(dateString, formatter);

        }  catch (IllegalArgumentException ignored) {
        }

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime twentyFourHoursLater = now.plusHours(24);

        boolean isNowOrLater = !targetDateTime.isBefore(now);
        boolean isBeforeCutoff = targetDateTime.isBefore(twentyFourHoursLater);

        return isNowOrLater && isBeforeCutoff;
    }

    public static boolean isBefore(String dateString) {
        String formatPattern = "yyyy-MM-dd HH:mm";
        LocalDateTime targetDateTime = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);

            targetDateTime = LocalDateTime.parse(dateString, formatter);

        }  catch (IllegalArgumentException ignored) {
        }

        LocalDateTime now = LocalDateTime.now();

        return targetDateTime.isBefore(now);
    }
}
