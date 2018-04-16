package newbs.etranz;

import java.sql.Time;
import java.util.Date;

/**
 * Created by visabr on 2018-03-26.
 */

public class Trip_Data {
    String fromCity, toCity;
    String freeSpace;
    String price;
    String departure;
    String departureTime;
    String uid;

    public Trip_Data(String fromCity, String toCity, String freeSpace, String price, String departure, String departureTime, String uuid) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.freeSpace = freeSpace;
        this.price = price;
        this.departure = departure;
        this.departureTime = departureTime;
        this.uid = uuid;
    }
}
