package newbs.etranz;

import java.util.Date;

/**
 * Created by visabr on 2018-03-19.
 */

public class UserData {
    public String usrName;
    public String birthDay;
    public int rating;

    public UserData(String usrName, String birthDay)
    {
        this.usrName = usrName;
        this.birthDay = birthDay;
        this.rating = 0;
    }
}
