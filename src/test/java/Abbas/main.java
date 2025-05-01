package Abbas;

import com.flightbooking.Booking.BookTickets;
import com.flightbooking.SearchEngine.Search;
import com.flightbooking.Terminal.Menu;
import com.flightbooking.Database.Data;

public class main {
    public static void main(String[] args) {
        Data base = Data.loadFromFile();
        Search engine = new Search(base);
        BookTickets ticket = new BookTickets(engine);
        Menu menu = new Menu(ticket, engine);
        menu.showMainMenu();
    }
}
