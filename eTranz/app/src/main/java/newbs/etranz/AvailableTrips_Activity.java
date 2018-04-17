package newbs.etranz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AvailableTrips_Activity extends AppCompatActivity {

    private ListView lvTrip;
    private TripListAdapter adapter;
    private List<Trip_Data> mTripList;
    private String fromCity;
    private String toCity;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeObjects();
        //Adding data
        mTripList = new ArrayList<>();
        Random random = new Random();
        int freeSpace = 1 + random.nextInt(4);
        int price = random.nextInt(8);
        mTripList.add(new Trip_Data(fromCity,toCity,""+freeSpace,""+price,
                "departure","15:20","Linas"));
        price = random.nextInt(8);
        mTripList.add(new Trip_Data(fromCity,toCity,""+freeSpace,""+price,
                "departure","19:45","Agnė"));
        price = random.nextInt(8);
        mTripList.add(new Trip_Data(fromCity,toCity,""+freeSpace,""+price,
                "departure","21:00","Paulius"));
        price = random.nextInt(8);
        mTripList.add(new Trip_Data(fromCity,toCity,""+freeSpace,""+price,
                "departure","9:00","Tomas"));

        adapter = new TripListAdapter(getApplicationContext(), mTripList);
        lvTrip.setAdapter(adapter);

        lvTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Clicked product id =" + view.getTag(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeObjects() {
        setContentView(R.layout.activity_available_trips);
        //Getting fromCity and toCity variables from TripSearch_Activity
        Intent intent = getIntent();
        fromCity = intent.getStringExtra(TripSearch_Activity.EXTRA_FROM_CITY);
        toCity = intent.getStringExtra(TripSearch_Activity.EXTRA_TO_CITY);

        lvTrip = (ListView)findViewById(R.id.listView_trip);
    }
}
