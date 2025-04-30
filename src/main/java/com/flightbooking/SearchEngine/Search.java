package com.flightbooking.SearchEngine;

import com.flightbooking.Flight;
import com.flightbooking.Ticket;
import com.flightbooking.User;
import com.flightbooking.database.Data;

import java.util.ArrayList;
import java.util.List;

public class Search {

    public Data data;

    public Search(Data data){
        this.data = data;
    }

    /**
     * Filters the whole ticket list and only gives those tickets which are relatable with the credentials.
     *
     * @param name name of the ticket owner
     * @param surname surname of the ticket owner
     * @return list of filtered tickets
     */
    public List<Ticket> filterTickets(String name, String surname){
        User user = filterUser(name, surname);
        List<Ticket> filtered_ticket = new ArrayList<>();
        if(user != null){
            for(Ticket atr: data.getTickets()){
                if(atr.getUserId().equals(user.getUserId())){
                    filtered_ticket.add(atr);
                }
            }
        }
        return filtered_ticket;
    }

    /**
     * Filters the flights.
     *
     * @param destination destination
     * @param date date
     * @param quantity quantity
     * @return list of available flights
     */
    public List<Flight> filterFlights(String destination, String date, int quantity){

        if (data == null || data.getFlights() == null) {
            return new ArrayList<>();
        }

        List<Flight> list = data.getFlights();
        List<Flight> temp = new ArrayList<>();
        for(Flight atr : list){
            String[] tempDate = atr.getDateAndTime().split(" ");
            if(atr.getDestination().equals(destination)
                    && atr.getAvailableSeats() >= quantity && tempDate[0].equals(date)){
                temp.add(atr);
            }
        }
        return temp;
    }

    /**
     * Finds the flight which has the given ID.
     * @param ID ID of the flight
     * @return null if specific ID could not be found, but if it is found, then the flight info
     */
    public Flight findFlight(String ID){
        List<Flight> list = data.getFlights();
        for(Flight atr : list){
            if(atr.getFlightId().equals(ID)){
                return atr;
            }
        }
        return null;
    }


    public User findUser(String ID){
        List<User> list = data.getUsers();
        for(User atr : list){
            if(atr.getUserId().equals(ID)){
                return atr;
            }
        }
        return null;
    }

    /**
     * Finds the ticket which has the given ID.
     * @param ID ID of the ticket
     * @return null if specific ID could not be found, but if it is found, then the ticket info
     */
    public Ticket findTicket(String ID){
        List<Ticket> list = data.getTickets();
        for(Ticket atr : list){
            if(atr.getTicketId().equals(ID)){
                return atr;
            }
        }
        return null;
    }

    /**
     * Finds the user by its name and surname.
     * @param name name of the ticket owner
     * @param surname surname of the ticket owner
     * @return all data of the user which has exact name and surname
     */
    public User filterUser(String name, String surname){
        List<User> users = data.getUsers();
        for(User atr : users){
            if(atr.getName().equals(name) && atr.getSurname().equals(surname)){
                return atr;
            }
        }
        return null;
    }

}
