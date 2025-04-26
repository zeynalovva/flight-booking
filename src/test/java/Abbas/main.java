package Abbas;

import com.flightbooking.Flight;
import com.flightbooking.SearchEngine.Search;
import com.flightbooking.database.Data;

import java.util.List;

public class main {
    public static void main(String[] args) {
        Data data = Data.loadFromFile();
        List<Flight> list = data.getFlights();
        Flight temp = null;
        for(Flight i : list){
            temp = i;
        }
        System.out.println(temp);
        //System.out.println(list);
        temp.setAvailableSeats(0);

        System.out.println(list);

    }
}
