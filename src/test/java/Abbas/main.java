package Abbas;

import com.flightbooking.Booking.BookTickets;
import com.flightbooking.Flight;
import com.flightbooking.SearchEngine.Search;
import com.flightbooking.Terminal.Menu;
import com.flightbooking.database.Data;

import java.util.List;

public class main {
    public static void main(String[] args) {
        Data base = new Data();
        Search engine = new Search(base);
        BookTickets ticket = new BookTickets(engine);
        Menu menu = new Menu(ticket, engine);

        System.out.println(base.getFlights());
        System.out.println(base.getUsers());
        //menu.showMainMenu();
    }
}
