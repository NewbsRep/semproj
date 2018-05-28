package newbs.etranz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Selected_Trip_Activity extends AppCompatActivity {
    private CircleImageView civPhoto;
    private TextView tvDriver, tvPhone, tvDate, tvTime, tvFreeSeats, tvDriverRating, tvPrice;
    private TextView tvToCity, tvFromCity, tvDescription, tvRate;
    private Button btnReserve;
    private ImageView call, message;

    boolean btnPressed = false;

    private String departureDate;
    private Trip_Data trip;
    private boolean reserveButtonVisibility;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_trip);

        getExtrasFromIntent();
        initializeViews();
        populateViews();
        initializeRating();

        if(reserveButtonVisibility)
            btnReserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reserveSeat();
                }
            });
        else {
            btnReserve.setVisibility(View.INVISIBLE);
        }

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = tvPhone.getText().toString();
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                        "tel", phone, null));
                startActivity(phoneIntent);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + tvPhone.getText().toString()));
                startActivity(sendIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void initializeRating(){
        tvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog();
            }
        });
    }

    private void AlertDialog(){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.rate))
                .setMessage(getResources().getString(R.string.ratingTitle))
                .setNegativeButton("Neigiamai", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Selected_Trip_Activity.this, "Neigimas vertinimas", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Teigiamai", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Selected_Trip_Activity.this, "Teigiamas vertinimas", Toast.LENGTH_SHORT).show();
                    }
                }).setNeutralButton("Atšaukti", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Selected_Trip_Activity.this, "Neutralus", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
    }

    private void reserveSeat() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map singleTrip = (Map) dataSnapshot.child("trips").child(departureDate).child(trip.getTripKey()).getValue();
                String stringFreeSeats = (String) singleTrip.get("freeSpace");
                String stringPassengerCounter = (String) singleTrip.get("passengerCounter");
                int passengerCounter = Integer.parseInt(stringPassengerCounter);
                int freeSeats = Integer.parseInt(stringFreeSeats);
                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (freeSeats > 0) {
                    if(trip.getUid().equals(userUid)) { // Check if driver
                        Toast.makeText(getApplicationContext(), "Jūs vairuotojas!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    DataSnapshot help = dataSnapshot.child("trips").child(departureDate).child(trip.getTripKey()).child("passengers");
                    if(help.exists()) {
                        ArrayList<String> passengers = (ArrayList<String>) help.getValue();
                        for (int i = 0; i < passengers.size(); i++) {
                            String passenger = passengers.get(i);
                            if (passenger.equals(userUid)) {
                                Toast.makeText(getApplicationContext(), "Jūs jau užsirezervavę!", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }
                    Map<String, Object> passengerUpdates = new HashMap<>();
                    passengerUpdates.put(stringPassengerCounter, userUid);
                    databaseReference.child("trips").child(departureDate).child(trip.getTripKey()).child("passengers").updateChildren(passengerUpdates);

                    freeSeats--;
                    passengerCounter++;
                    Map<String, Object> tripUpdates = new HashMap<>();
                    tripUpdates.put("freeSpace", Integer.toString(freeSeats));
                    tripUpdates.put("passengerCounter", Integer.toString(passengerCounter));
                    databaseReference.child("trips").child(departureDate).child(trip.getTripKey()).updateChildren(tripUpdates);
                    tvFreeSeats.setText("" + freeSeats);

                    Toast.makeText(getApplicationContext(), "Sėkmingai rezervavote vietą!", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "Nebėra laisvų vietų!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Duomenų bazės klaida!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getExtrasFromIntent() {
        Intent intent = getIntent();
        departureDate = intent.getStringExtra("date");
        trip = intent.getParcelableExtra(AvailableTrips_Activity.EXTRA_TRIP);
        reserveButtonVisibility = intent.getBooleanExtra("isFromMyTrips", true);
    }

    private void initializeViews() {
        tvDriver = findViewById(R.id.tvDriver);
        tvPhone = findViewById(R.id.tvPhoneNum);
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
        call = findViewById(R.id.ivCall);
        message = findViewById(R.id.ivMessage);
        tvRate = findViewById(R.id.tvRate);
    }

    private void populateViews() {
        tvFromCity.setText(trip.getFromCity());
        tvToCity.setText(trip.getToCity());
        tvDate.setText(trip.getDeparture());
        tvTime.setText(trip.getDepartureTime());
        tvFreeSeats.setText(trip.getFreeSpace());
        tvPrice.setText(trip.getPrice());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("users").child(trip.getUid()).child("phone").exists())
                    tvPhone.setText(dataSnapshot.child("users").child(trip.getUid()).child("phone").getValue(String.class));
                if(dataSnapshot.child("users").child(trip.getUid()).child("usrName").exists())
                    tvDriver.setText(dataSnapshot.child("users").child(trip.getUid()).child("usrName").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

            }
        });

        storageReference = storageReference.child(trip.getUid()).child("Images").child("ProfilePic");
        Glide.with(Selected_Trip_Activity.this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .error(R.drawable.profile_img)
                .fitCenter()
                .into(civPhoto);
    }
}
