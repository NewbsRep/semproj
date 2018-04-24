package newbs.etranz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvailableTrips_Activity extends AppCompatActivity {

    private ListView lvTrip;
    private TripListAdapter adapter;
    private List<Trip_Data> mTripList;
    private String EXTRA_fromCity, EXTRA_toCity, departureDate;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeObjects();

        mTripList = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("trips").hasChild(departureDate)) {
                    fillAvailableTrips((Map<String, Object>) dataSnapshot.child("trips").child(departureDate).getValue(),
                            dataSnapshot.child("users"));
                    if(mTripList.size() != 0)
                        displayTrips();
                    else
                        Toast.makeText(getApplicationContext(), "Kelionių nerasta!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Kelionių nerasta!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AvailableTrips_Activity.this, TripSearch_Activity.class));
                }
            }

            private void fillAvailableTrips(Map<String, Object> trips, DataSnapshot user) {
                for(Map.Entry<String, Object> entry : trips.entrySet()) {
                    Map singleTrip = (Map) entry.getValue();

                    String uid = (String) singleTrip.get("uid");
                    Map userData = (Map) user.child(uid).getValue();

                    String fromCity = (String) singleTrip.get("fromCity");
                    String toCity = (String) singleTrip.get("toCity");
                    String freeSpace = (String) singleTrip.get("freeSpace");
                    String price = (String) singleTrip.get("price");
                    String departure = (String) singleTrip.get("departure");
                    String departureTime = (String) singleTrip.get("departureTime");
                    String driver = (String) userData.get("usrName");

                    if(fromCity.equals(EXTRA_fromCity) && toCity.equals(EXTRA_toCity))
                    mTripList.add(new Trip_Data(fromCity, toCity, freeSpace, price, departure,
                            departureTime, driver));
                }
            }

            private void displayTrips() {
                adapter = new TripListAdapter(getApplicationContext(), mTripList);
                lvTrip.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error",
                        Toast.LENGTH_SHORT).show();
            }
        });

        lvTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Driver = " + view.getTag(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeObjects() {
        setContentView(R.layout.activity_available_trips);

        //Getting variables from TripSearch_Activity
        Intent intent = getIntent();
        EXTRA_fromCity = intent.getStringExtra(TripSearch_Activity.EXTRA_FROM_CITY);
        EXTRA_toCity = intent.getStringExtra(TripSearch_Activity.EXTRA_TO_CITY);
        departureDate = intent.getStringExtra(TripSearch_Activity.EXTRA_DEPARTURE_DATE);

        lvTrip = (ListView)findViewById(R.id.listView_trip);
    }
}
