package newbs.etranz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Selected_Trip_Activity extends AppCompatActivity {
    private CircleImageView civPhoto;
    private TextView tvDriver, tvDriversCar, tvDate, tvTime, tvFreeSeats, tvDriverRating, tvPrice;
    private TextView tvToCity, tvFromCity, tvDescription;
    private Button btnReserve;

    private String departureDate;
    private Trip_Data trip;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_trip);

        getExtrasFromIntent();
        initializeViews();
        populateViews();

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map singleTrip = (Map) dataSnapshot.child("trips").child(departureDate).child(trip.getTripKey()).getValue();
                        String stringFreeSeats = (String) singleTrip.get("freeSpace");
                        int freeSeats = Integer.parseInt(stringFreeSeats);
                        if(freeSeats > 0) {
                            freeSeats--;
                            trip.setFreeSpace(""+freeSeats);
                            databaseReference.child("trips").child(departureDate).child(trip.getTripKey()).setValue(trip);
                            tvFreeSeats.setText(""+freeSeats);
                            Toast.makeText(getApplicationContext(), "Sėkmingai rezervavote vietą!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Nebėra laisvų vietų!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Duomenų bazės klaida!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void getExtrasFromIntent() {
        Intent intent = getIntent();
        departureDate = intent.getStringExtra("date");
        trip = intent.getParcelableExtra(AvailableTrips_Activity.EXTRA_TRIP);
    }

    private void initializeViews() {
        tvDriver = findViewById(R.id.tvDriver);
        tvDriversCar = findViewById(R.id.tvDriversCar);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvFreeSeats = findViewById(R.id.tvFreeSeats);
        tvDriverRating = findViewById(R.id.tvDriverRating);
        tvPrice = findViewById(R.id.tvPrice);
        tvToCity = findViewById(R.id.tvToCity);
        tvFromCity = findViewById(R.id.tvFromCity);
        tvDescription = findViewById(R.id.tvDescription);

        btnReserve = findViewById(R.id.btnTakeSeat);
        civPhoto = findViewById(R.id.civDriverPicture);
    }

    private void populateViews() {
        tvDriver.setText(trip.getDriverName());
        tvFromCity.setText(trip.getFromCity());
        tvToCity.setText(trip.getToCity());
        tvDate.setText(trip.getDeparture());
        tvTime.setText(trip.getDepartureTime());
        tvFreeSeats.setText(trip.getFreeSpace());
        tvPrice.setText(trip.getPrice());

        storageReference = storageReference.child(trip.getUid()).child("Images").child("ProfilePic");
        Glide.with(Selected_Trip_Activity.this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .error(R.drawable.profile_img)
                .fitCenter()
                .into(civPhoto);
    }
}
