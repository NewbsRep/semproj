package newbs.etranz;

import java.sql.Time;
import java.util.Date;

/**
 * Created by visabr on 2018-03-26.
 */

public class Trip_Data {
    private String fromCity, toCity;
    private String freeSpace;
    private String price;
    private String departure;
    private String departureTime;
    private String uid;

    //Constructor
    public Trip_Data(String fromCity, String toCity, String freeSpace, String price, String departure, String departureTime, String uid) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.freeSpace = freeSpace;
        this.price = price;
        this.departure = departure;
        this.departureTime = departureTime;
        this.uid = uid;
    }

    //Setter, getter
    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(String freeSpace) {
        this.freeSpace = freeSpace;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
