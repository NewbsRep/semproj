package newbs.etranz;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by visabr on 2018-03-26.
 */

public class Trip_Data implements Parcelable{
    private String fromCity, toCity;
    private String freeSpace;
    private String price;
    private String departure;
    private String departureTime;
    private String uid;

    private String driverName;
    private String tripKey;

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

    protected Trip_Data(Parcel in) {
        fromCity = in.readString();
        toCity = in.readString();
        freeSpace = in.readString();
        price = in.readString();
        departure = in.readString();
        departureTime = in.readString();
        uid = in.readString();
        driverName = in.readString();
        tripKey = in.readString();
    }

    public static final Creator<Trip_Data> CREATOR = new Creator<Trip_Data>() {
        @Override
        public Trip_Data createFromParcel(Parcel in) {
            return new Trip_Data(in);
        }

        @Override
        public Trip_Data[] newArray(int size) {
            return new Trip_Data[size];
        }
    };

    //Setter, getter
    public String getTripKey() {
        return tripKey;
    }

    public void setTripKey(String tripKey) {
        this.tripKey = tripKey;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fromCity);
        parcel.writeString(toCity);
        parcel.writeString(freeSpace);
        parcel.writeString(price);
        parcel.writeString(departure);
        parcel.writeString(departureTime);
        parcel.writeString(uid);
        parcel.writeString(driverName);
        parcel.writeString(tripKey);
    }
}
