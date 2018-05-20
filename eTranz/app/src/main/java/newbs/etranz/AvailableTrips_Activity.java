package newbs.etranz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

    public static final String EXTRA_TRIP = "newbs.etranz.EXTRA_TRIP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeObjects();

        updateListView();

        lvTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Trip_Data a = (Trip_Data) view.getTag(); // Parselable Tag
                openSelectedTripActivity(a);
            }
        });
    }

    private void updateListView() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTripList = new ArrayList<>();
                if(dataSnapshot.child("trips").hasChild(departureDate)) {
                    Map<String, Object> tripsOnDate = (Map<String, Object>) dataSnapshot.child("trips").child(departureDate).getValue();
                    DataSnapshot users = dataSnapshot.child("users");
                    fillTripDataList(tripsOnDate, users);
                    if(mTripList.size() != 0)
                        displayTrips();
                    else
                        Toast.makeText(getApplicationContext(), "Tinkamų kelionių nerasta!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Kelionių nerasta tą dieną!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AvailableTrips_Activity.this, TripSearch_Activity.class));
                }
            }

            private void fillTripDataList(Map<String, Object> trips, DataSnapshot users) {
                for(Map.Entry<String, Object> entry : trips.entrySet()) {
                    String tripKey = entry.getKey();
                    Map singleTrip = (Map) entry.getValue();

                    String uid = (String) singleTrip.get("uid");
                    Map userData = (Map) users.child(uid).getValue();

                    String fromCity = (String) singleTrip.get("fromCity");
                    String toCity = (String) singleTrip.get("toCity");
                    String freeSpace = (String) singleTrip.get("freeSpace");
                    String price = (String) singleTrip.get("price");
                    String departure = (String) singleTrip.get("departure");
                    String departureTime = (String) singleTrip.get("departureTime");
                    String driver = (String) userData.get("usrName");

                    int freeSeats = Integer.parseInt(freeSpace);
                    if(fromCity.equals(EXTRA_fromCity) && toCity.equals(EXTRA_toCity) && freeSeats > 0) {
                        Trip_Data availableTrip = new Trip_Data(fromCity, toCity, freeSpace, price, departure,
                                departureTime, uid);
                        availableTrip.setDriverName(driver);
                        availableTrip.setTripKey(tripKey);
                        mTripList.add(availableTrip);
                    }
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
    }

    private void openSelectedTripActivity(Trip_Data extra) {
        Intent intent = new Intent(this, Selected_Trip_Activity.class);
        intent.putExtra("date", departureDate);
        intent.putExtra(EXTRA_TRIP,  extra);
        startActivity(intent);
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
