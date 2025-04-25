package Abbas;

import com.flightbooking.SearchEngine.Search;
import com.flightbooking.database.Data;

public class main {
    public static void main(String[] args) {
        Data data = Data.loadFromFile();
        Search search = new Search(data);
        System.out.println(search.filterFlights("New York", "2025-04-28", 11));

    }
}
