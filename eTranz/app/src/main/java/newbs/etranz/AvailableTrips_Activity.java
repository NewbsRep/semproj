package newbs.etranz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AvailableTrips_Activity extends AppCompatActivity {

    private ListView lvTrip;
    private TripListAdapter adapter;
    private List<Trip_Data> mTripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_trips);

        lvTrip = (ListView)findViewById(R.id.listView_trip);

        mTripList = new ArrayList<>();
        //Adding data
        mTripList.add(new Trip_Data("Kaunas","Vilnius","3","5",
                "departure","15:20","Linas"));
        mTripList.add(new Trip_Data("Kaunas","Klaipeda","2","4,5",
                "departure","19:45","Agnė"));
        mTripList.add(new Trip_Data("Vilnius","Klaipėda","3","5",
                "departure","21:00","Paulius"));
        mTripList.add(new Trip_Data("Vilnius","Kaunas","3","5",
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
}
