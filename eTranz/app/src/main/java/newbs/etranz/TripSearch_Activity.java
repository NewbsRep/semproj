package newbs.etranz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TripSearch_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
