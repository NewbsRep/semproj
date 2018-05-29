package newbs.etranz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private EditText etDate, etTime, erFrom, erTo, erDate, erTime;
    private String searchDate; // January = 0
    private static final int TIME_ID = 0;
    private static final int DATE_ID = 1;

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
                erTime.setError(null);
                showDialog(TIME_ID);
            }
        });
    }

    protected void showDateDialog() {
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                erDate.setError(null);
                showDialog(DATE_ID);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trip_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnConfirm) {
            searchForTrip();
        }
        else if(id == android.R.id.home)
            onBackPressed();
        return true;
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
        getSupportActionBar().setTitle("Ieškoti kelionės");
        initializeObj();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TripSearch_Activity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.cities));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
        fromSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        erFrom.setError(null);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        toSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        erTo.setError(null);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        showDateDialog();
        showTimeDialog();
    }

    public void searchForTrip(){
        if(!noEmptyFields())
            return;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(searchDate))
                    Toast.makeText(getApplicationContext(), "Pasirinktą dieną kelionių nerasta!",
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
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        finish();
//        return true;
//    }

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

    private void initializeObj() {
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        fromSpinner.setTitle("Pasirinkite išvykimo miesta");
        toSpinner.setTitle("Pasirinkite atvykimo miestą");
        toSpinner.setPositiveButton("Atšaukti");
        fromSpinner.setPositiveButton("Atšaukti");
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        erFrom = findViewById(R.id.erFrom);
        erTo = findViewById(R.id.erTo);
        erTime = findViewById(R.id.erTime);
        erDate = findViewById(R.id.erDate);
    }

    private boolean noEmptyFields() {
        int posFrom = fromSpinner.getSelectedItemPosition();
        int posTo = toSpinner.getSelectedItemPosition();
        if (posFrom == -1) {
            erFrom.setError(getResources().getString(R.string.emptyFieldMsg));
            erFrom.requestFocus();
            return false;
        } else if (posTo == -1) {
            erTo.setError(getResources().getString(R.string.emptyFieldMsg));
            erTo.requestFocus();
            return false;
        } else if (posFrom == posTo) {
            erTo.setError(getResources().getString(R.string.sameTownsMsg));
            erTo.requestFocus();
            return false;
        } else if (etDate.getText().toString().isEmpty()) {
            erDate.setError(getResources().getString(R.string.emptyFieldMsg));
            erDate.requestFocus();
            return false;
        } else if (etTime.getText().toString().isEmpty()) {
            erTime.setError(getResources().getString(R.string.emptyFieldMsg));
            erTime.requestFocus();
            return false;
        }
        return true;
    }

}
