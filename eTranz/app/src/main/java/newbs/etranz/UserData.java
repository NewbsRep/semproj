package newbs.etranz;

import java.util.Date;

/**
 * Created by visabr on 2018-03-19.
 */

public class UserData {
    private String usrName, birthDay, phone;
    private int rating;

    public UserData(){}

    public UserData(String usrName, String birthDay, String phone)
    {
        this.usrName = usrName;
        this.birthDay = birthDay;
        this.phone = phone;
        this.rating = 0;
    }

    public String getUsrName() {
        if (usrName != null){
            return usrName;
        }

        return null;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phoneNum) {
        this.phone= phoneNum;
    }
}
