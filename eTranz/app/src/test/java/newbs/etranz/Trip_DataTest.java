package newbs.etranz;

import org.junit.Test;

import static org.junit.Assert.*;

public class Trip_DataTest {

    Trip_Data trip = new Trip_Data("Ukmerge", "Kaunas", "3", "5", "Vytauto 35", "14:30", "03");

    @Test
    public void getTripKey() {
        trip.setTripKey("21s5");
        assertEquals(trip.getTripKey(), "21s5");
    }

    @Test
    public void setTripKey() {
        trip.setTripKey("2236");
        assertEquals(trip.getTripKey(), "2236");
    }

    @Test
    public void getDriverName() {
        trip.setDriverName("Rodzeris");
        assertEquals(trip.getDriverName(), "Rodzeris");
    }

    @Test
    public void setDriverName() {
        trip.setDriverName("Elvis");
        assertEquals(trip.getDriverName(), "Elvis");
    }

    @Test
    public void getFromCity() {
        assertEquals(trip.getFromCity(), "Ukmerge");
    }

    @Test
    public void setFromCity() {
        trip.setFromCity("Vilnius");
        assertEquals(trip.getFromCity(), "Vilnius");
    }

    @Test
    public void getToCity() {
        assertEquals(trip.getToCity(), "Kaunas");
    }

    @Test
    public void setToCity() {
        trip.setToCity("Klaipeda");
        assertEquals(trip.getToCity(), "Klaipeda");
    }

    @Test
    public void getFreeSpace() {
        assertEquals(trip.getFreeSpace(), "3");
    }

    @Test
    public void setFreeSpace() {
        trip.setFreeSpace("1");
        assertEquals(trip.getFreeSpace(), "1");
    }

    @Test
    public void getPrice() {
        assertEquals(trip.getPrice(), "5");
    }

    @Test
    public void setPrice() {
        trip.setPrice("20");
        assertEquals(trip.getPrice(), "20");
    }

    @Test
    public void getDeparture() {
        assertEquals(trip.getDeparture(), "Vytauto 35");
    }

    @Test
    public void setDeparture() {
        trip.setDeparture("Berzu 1");
        assertEquals(trip.getDeparture(), "Berzu 1");
    }

    @Test
    public void getDepartureTime() {
        assertEquals(trip.getDepartureTime(), "14:30");
    }

    @Test
    public void setDepartureTime() {
        trip.setDepartureTime("09:00");
        assertEquals(trip.getDepartureTime(), "09:00");
    }

    @Test
    public void getUid() {
        assertEquals(trip.getUid(), "03");
    }

    @Test
    public void setUid() {
        trip.setUid("236");
        assertEquals(trip.getUid(), "236");
    }
}