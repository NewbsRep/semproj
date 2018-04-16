package newbs.etranz;

import java.util.Date;

/**
 * Created by visabr on 2018-03-19.
 */

public class UserData {
    private String usrName, birthDay;
    private int rating;

    public UserData(){}

    public UserData(String usrName, String birthDay)
    {
        this.usrName = usrName;
        this.birthDay = birthDay;
        this.rating = 0;
    }

    public String getUsrName() {
        if (usrName != null){
            return usrName;
        }

        return "";
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
