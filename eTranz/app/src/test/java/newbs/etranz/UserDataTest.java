package newbs.etranz;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserDataTest {

    UserData test = new UserData("Petronis", "1992-11-12");

    @Test
    public void getUsrName() {
        assertEquals("Petronis", test.getUsrName());
    }

    @Test
    public void getUsrNameFail() {
        assertNotEquals("Petrona", test.getUsrName());
    }

    @Test
    public void setUsrName() {
        test.setUsrName("kebab112");
        assertEquals("kebab112", test.getUsrName());
    }

    @Test
    public void setUsrNameFail() {
        test.setUsrName("kebab");
        assertNotEquals("kebab112", test.getUsrName());
    }

    @Test
    public void getBirthDay() {
        assertEquals("1992-11-12", test.getBirthDay());
    }

    @Test
    public void getBirthDayFail() {
        assertNotEquals("1992-1112", test.getBirthDay());
    }

    @Test
    public void setBirthDay() {
        test.setBirthDay("1997-11-12");
        assertEquals("1997-11-12", test.getBirthDay());
    }

    @Test
    public void setBirthDayFail() {
        test.setBirthDay("19971112");
        assertNotEquals("1997-11-12", test.getBirthDay());
    }

    @Test
    public void getRating() {
        assertEquals(0, test.getRating());
    }

    @Test
    public void getRatingFail() {
        assertNotEquals(1, test.getRating());
    }


    @Test
    public void setRating() {
        test.setRating(56);
        assertEquals(56, test.getRating());
    }

    @Test
    public void setRatingFail() {
        test.setRating(56);
        assertNotEquals(0, test.getRating());
    }
}