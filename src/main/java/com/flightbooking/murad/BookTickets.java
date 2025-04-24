package com.flightbooking.murad;

import java.util.Scanner;
import java.util.UUID;

public class BookTickets {
    private Database database;
    public BookTickets(Database database) {
        this.database = database;
    }
    public void StartBookTickets(String bookerId, String flightId, List<String> passengerIds) {
        try {
            List<Flight> filteredFlights = filterFlights();
            displayFlights(filteredFlights);
            Flight selectedFlight = selectFlight(filteredFlights);
            if (selectedFlight == null) return;//display the Filtered flights
        } catch (Exception e) {
            System.out.printf("Error: " + e.getMessage());;
        }
    }


    public void cancelTickets(String TicketId){}


    private void displayFlights(List<Flight> flights) {
        System.out.println("\nAvailable Flights:");
        for (int i = 0; i < flights.size(); i++) {
            Flight f = flights.get(i);
            System.out.printf("%d. %s | %s | Seats: %d | Date: %s\n",
                    i + 1, f.getFlightId(), f.getDestination(),
                    f.getFreeSeats(), f.getDate());
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

    //enter booker credentials
    private void enterBookerCredentials() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.printf("Enter your surname: ");
        String surname = scanner.nextLine();
        database.saveBooker(surname, surname, UUID.randomUUID().toString());
    }


}
