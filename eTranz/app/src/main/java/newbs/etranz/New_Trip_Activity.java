package newbs.etranz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class New_Trip_Activity extends AppCompatActivity {
    private EditText etDate, etTime;
    private ImageView seatInc, seatDec, priceInc, priceDec;
    private TextView tvPrice, tvSeats;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static final int TIME_ID = 0;
    private static final int DATE_ID = 1;
    private SearchableSpinner fromSpinner, toSpinner;
    private int seatCounter;
    private double price;
    private String ogDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__trip_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeObj();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(New_Trip_Activity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.cities));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
        showDateDialog();
        showTimeDialog();
        seatCount();
        setPrice();
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

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(New_Trip_Activity.this, dateListener, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

        if (id == TIME_ID)
            return new TimePickerDialog(New_Trip_Activity.this, timeOnClickListener, c.getTime().getHours(), c.getTime().getMinutes(), true);
        else if (id == DATE_ID)
            return datePickerDialog;
        else return null;
    }

    protected DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            ogDate = String.format("%d-%02d-%02d ", year, month, dayOfMonth);
            String date = year + getString(R.string.DATE_FORMAT) + determineMonth(month) + dayOfMonth
                    + getString(R.string.WORD_DAY);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trip_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnConfirm) {
            uploadData();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initializeObj() {
        etDate = findViewById(R.id.etDate);
        tvSeats = findViewById(R.id.tvSeats);
        tvPrice = findViewById(R.id.tvPrice);
        etTime = findViewById(R.id.etTime);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        fromSpinner.setTitle("Pasirinkite išvykimo miesta");
        toSpinner.setTitle("Pasirinkite atvykimo miestą");
        toSpinner.setPositiveButton("Atšaukti");
        fromSpinner.setPositiveButton("Atšaukti");
        priceDec = findViewById(R.id.ivPriceDec);
        priceInc = findViewById(R.id.ivPriceInc);
        seatDec = findViewById(R.id.ivSeatDec);
        seatInc = findViewById(R.id.ivSeatInc);
        seatCounter = 0;
        price = 0;
    }

    public void uploadData() {
        final ProgressDialog pd = new ProgressDialog(New_Trip_Activity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Kelionė išsaugoma...");
        pd.show();
        String seats = tvSeats.getText().toString();
        String price = tvPrice.getText().toString();
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();

        if (fromSpinner.equals(null) || toSpinner.equals(null) || seats.equals("0") || price.isEmpty() ||
        date.isEmpty() || time.isEmpty()){
            pd.cancel();
            Toast.makeText(this, this.getResources().getText(R.string.emptyFieldMsg), Toast.LENGTH_SHORT).show();
        }
        else {
            String from = fromSpinner.getSelectedItem().toString();
            String to = toSpinner.getSelectedItem().toString();
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Trip_Data data = new Trip_Data(from, to, seats, price, date, time, uid);
            DatabaseReference ref = firebaseDatabase.getReference();
            String key = ref.child("trips").push().getKey();
            Task upload = ref.child("trips").child(ogDate).child(key).setValue(data);
            upload.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    pd.cancel();
                    Toast.makeText(New_Trip_Activity.this, "Skelbimas pridėtas", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(New_Trip_Activity.this, HomeScreen_Activity.class));
                }
            });
            upload.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.cancel();
                    Toast.makeText(New_Trip_Activity.this, "Duomenų bazės klaida", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String determineMonth(int month) {
        switch(month) {
            case 0:
                return getString(R.string.MONTH_JANUARY);
            case 1:
                return getString(R.string.MONTH_FEBRUARY);
            case 2:
                return getString(R.string.MONTH_MARCH);
            case 3:
                return getString(R.string.MONTH_APRIL);
            case 4:
                return getString(R.string.MONTH_MAY);
            case 5:
                return getString(R.string.MONTH_JUNE);
            case 6:
                return getString(R.string.MONTH_JULY);
            case 7:
                return getString(R.string.MONTH_AUGUST);
            case 8:
                return getString(R.string.MONTH_SEPTEMBER);
            case 9:
                return getString(R.string.MONTH_OCTOBER);
            case 10:
                return getString(R.string.MONTH_NOVEMBER);
            case 11:
                return getString(R.string.MONTH_DECEMBER);
        }
        return getString(R.string.ERROR);
    }

    public void seatCount(){
        seatInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seatCounter < 7)
                    seatCounter++;
                tvSeats.setText(String.valueOf(seatCounter));
            }
        });

        seatDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seatCounter > 0)
                    seatCounter--;
                tvSeats.setText(String.valueOf(seatCounter));
            }
        });
    }

    public void setPrice(){
        final DecimalFormat format = new DecimalFormat("0.#");
        priceInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price+= 0.5;
                String line = format.format(price)+"€";
                tvPrice.setText(line);
            }
        });

        priceDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(price > 0)
                    price-=0.5;
                String line = format.format(price)+"€";
                tvPrice.setText(line);
            }
        });
    }
}
