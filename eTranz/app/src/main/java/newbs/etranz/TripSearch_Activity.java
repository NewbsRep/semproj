package newbs.etranz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.Calendar;

public class TripSearch_Activity extends AppCompatActivity {
    public static final String EXTRA_FROM_CITY = "newbs.etranz.EXTRA_FROM_CITY";
    public static final String EXTRA_TO_CITY = "newbs.etranz.EXTRA_TO_CITY";
    public static final String EXTRA_DEPARTURE_DATE = "newbs.etranz.EXTRA_DEPARTURE_DATE";

    private SearchableSpinner fromSpinner, toSpinner;
    private Button searchButton;
    private EditText etDate, etTime;
    private String searchDate; // January = 0
    static final int TIME_ID = 0;
    static final int DATE_ID = 1;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference().child("trips");


    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(TripSearch_Activity.this, dateListener, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

        if (id == TIME_ID)
            return new TimePickerDialog(TripSearch_Activity.this, timeOnClickListener, c.getTime().getHours(), c.getTime().getMinutes(), true);
        else if (id == DATE_ID)
            return datePickerDialog;
        else return null;
    }

    protected void showTimeDialog() {
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_ID);
            }
        });
    }

    protected void showDateDialog() {
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_ID);
            }
        });
    }

    protected DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String date = String.format("%d-%02d-%02d ", year, month + 1, dayOfMonth);
            searchDate = String.format("%d-%02d-%02d ", year, month, dayOfMonth);
            etDate.setText(date);
        }
    };

    protected TimePickerDialog.OnTimeSetListener timeOnClickListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String curTime = String.format("%02d:%02d", hourOfDay, minute);
            etTime.setText(curTime);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeObj();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TripSearch_Activity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.cities));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
        showDateDialog();
        showTimeDialog();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(fromSpinner.getSelectedItem() == null || toSpinner.getSelectedItem() == null
                                || etDate.getText().toString().equals(""))
                            Toast.makeText(getApplicationContext(), "Užpildykite visus laukelius",
                                    Toast.LENGTH_SHORT).show();
                        else if(!dataSnapshot.hasChild(searchDate))
                            Toast.makeText(getApplicationContext(), "Pasirinktą dieną kelionių nerasta",
                                    Toast.LENGTH_SHORT).show();
                        else openFoundTripListActivity();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Database error",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void openFoundTripListActivity() {
        Intent intent = new Intent(this, AvailableTrips_Activity.class);
        String fromCity = fromSpinner.getSelectedItem().toString();
        String toCity = toSpinner.getSelectedItem().toString();
        String departureDate = searchDate;
        intent.putExtra(EXTRA_FROM_CITY, fromCity);
        intent.putExtra(EXTRA_TO_CITY, toCity);
        intent.putExtra(EXTRA_DEPARTURE_DATE, departureDate);
        startActivity(intent);
    }

    private void initializeObj(){
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        fromSpinner.setTitle("Pasirinkite išvykimo miesta");
        toSpinner.setTitle("Pasirinkite atvykimo miestą");
        toSpinner.setPositiveButton("Atšaukti");
        fromSpinner.setPositiveButton("Atšaukti");
        searchButton = findViewById(R.id.btnSearch);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
    }
}
