package newbs.etranz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class New_Trip_Activity extends AppCompatActivity {
    EditText etfrom, etTo, etDate, etTime, etFreeSpace, etPrice;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    static final int TIME_ID = 0;
    static final int DATE_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__trip_);
        initializeObj();
        showDateDialog();
        showTimeDialog();
    }

    protected void showTimeDialog(){
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
    protected Dialog onCreateDialog(int id){
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        if(id == TIME_ID)
            return new TimePickerDialog(New_Trip_Activity.this, timeOnClickListener, c.getTime().getHours() , c.getTime().getMinutes(), true);
        else if (id == DATE_ID)
            return new DatePickerDialog(New_Trip_Activity.this, dateListener, mYear, mMonth, mDay);
        else return null;
    }

    protected  DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String date = String.format("%d-%02d-%02d ", year, month, dayOfMonth);
            etDate.setText(date);
        }
    };

    protected TimePickerDialog.OnTimeSetListener timeOnClickListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
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
            Toast.makeText(New_Trip_Activity.this, "Skelbimas pridėtas", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(New_Trip_Activity.this, HomeScreen_Activity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void initializeObj() {
        etDate = findViewById(R.id.etDate);
        etFreeSpace = findViewById(R.id.etFreeSpace);
        etfrom = findViewById(R.id.etFrom);
        etTo = findViewById(R.id.etTo);
        etPrice = findViewById(R.id.etPrice);
        etTime = findViewById(R.id.etTime);
    }

    public void uploadData(){
        Trip_Data data = new Trip_Data(etfrom.getText().toString(), etTo.getText().toString(), etFreeSpace.getText().toString(),
                etPrice.getText().toString(), etDate.getText().toString(), etTime.getText().toString());
        DatabaseReference ref = firebaseDatabase.getReference();
        String key = ref.child("trips").push().getKey();
        ref.child("trips").child(key).setValue(data).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(New_Trip_Activity.this, "Duomenų bazės klaida", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
