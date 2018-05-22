package newbs.etranz;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyTrips_Activity extends AppCompatActivity {

    private static final String TAG = "MyTrips_Activity";

    private MyTrips_SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    private ListView lvDriverTrips;
    private ListView lvPassengerTrips;

    private TripListAdapter adapter;

    private List<Trip_Data> mDriverTripList;
    private List<Trip_Data> mPassengerTripList;

    private Trip_Data tripData;
    private String driverName;
    private String fromCity, toCity, freeSpace, price, departure, departureTime, driver;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    public static final String EXTRA_TRIP = "newbs.etranz.EXTRA_TRIP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeObjects();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        updateListView();

    }

    public void updateListView(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                initializeListViews();

                // Generate driver's trip list

                for (DataSnapshot date : dataSnapshot.child("trips").getChildren()){
                    for (DataSnapshot trip : date.getChildren()){
                        String given_uid = trip.child("uid").getValue().toString();
                        String needed_uid = firebaseAuth.getUid();
                        if (given_uid.equals(needed_uid)){
                            driverName = dataSnapshot.child("users").child(needed_uid).child("usrName").getValue().toString();
                            addTripToDriverList(trip);
                        }
                    }
                }

                if(mDriverTripList.size() != 0)
                    displayDriverTrips();
                else
                    Toast.makeText(getApplicationContext(), "DRIVER kelionių nerasta!", Toast.LENGTH_LONG).show();


                // Generate passenger's trip list

                for (DataSnapshot date : dataSnapshot.child("trips").getChildren()){
                    for (DataSnapshot trip : date.getChildren()){
                        if(trip.hasChild("passengers")){
                            String given_uid;
                            String needed_uid = firebaseAuth.getUid();

                            for (DataSnapshot passenger : trip.child("passengers").getChildren()){
                                given_uid = passenger.getValue().toString();
                                if(needed_uid.equals(given_uid)){
                                    String driver = trip.child("uid").getValue().toString();
                                    driverName = dataSnapshot.child("users").child(driver).child("usrName").getValue().toString();
                                    addTripToPassengerList(trip);
                                }
                            }
                        }
                    }
                }

                if(mPassengerTripList.size() != 0)
                    displayPassengerTrips();
                else
                    Toast.makeText(getApplicationContext(), "PASSENGER kelionių nerasta!", Toast.LENGTH_LONG).show();
            }

            private void initializeListViews(){
                lvDriverTrips = findViewById(R.id.listView_MyTrips_Driver);
                lvPassengerTrips = findViewById(R.id.listView_MyTrips_Passenger);

                lvDriverTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Trip_Data a = (Trip_Data) view.getTag(); // Parselable Tag
                        openSelectedTripActivity(a);
                    }
                });

                lvPassengerTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Trip_Data a = (Trip_Data) view.getTag(); // Parselable Tag
                        openSelectedTripActivity(a);
                    }
                });
            }

            private void addTripToDriverList(DataSnapshot trip){
                mDriverTripList.add(fillTripData(trip));
            }

            private void addTripToPassengerList(DataSnapshot trip){
                mPassengerTripList.add(fillTripData(trip));
            }

            private Trip_Data fillTripData(DataSnapshot trip){
                fromCity = (String) trip.child("fromCity").getValue();
                toCity = (String) trip.child("toCity").getValue();
                freeSpace = (String) trip.child("freeSpace").getValue();
                price = (String) trip.child("price").getValue();
                departure = (String) trip.child("departure").getValue();
                departureTime = (String) trip.child("departureTime").getValue();
                driver = (String) trip.child("uid").getValue();

                tripData = new Trip_Data(fromCity, toCity, freeSpace, price, departure, departureTime, driver);
                tripData.setDriverName(driverName);
                return tripData;
            }

            private void displayDriverTrips() {
                adapter = new TripListAdapter(getApplicationContext(), mDriverTripList);
                lvDriverTrips.setAdapter(adapter);
            }

            private void displayPassengerTrips() {
                adapter = new TripListAdapter(getApplicationContext(), mPassengerTripList);
                lvPassengerTrips.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager){
        MyTrips_SectionsPageAdapter adapter = new MyTrips_SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new MyTrips_Driver_Fragment(), "Vairuoju");
        adapter.addFragment(new MyTrips_Passenger_Fragment(), "!Vairuoju");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void openSelectedTripActivity(Trip_Data extra) {
        Intent intent = new Intent(this, Selected_Trip_Activity.class);
        intent.putExtra("date", extra.getDeparture());
        intent.putExtra(EXTRA_TRIP,  extra);
        intent.putExtra("isFromMyTrips", false);
        startActivity(intent);
    }

    private void initializeObjects() {
        setContentView(R.layout.activity_my_trips);
        Log.d(TAG, "onCreate: Starting.");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPageAdapter = new MyTrips_SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        mDriverTripList = new ArrayList<>();
        mPassengerTripList = new ArrayList<>();
    }
}
