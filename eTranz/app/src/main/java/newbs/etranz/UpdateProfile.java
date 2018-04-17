package newbs.etranz;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class UpdateProfile extends AppCompatActivity {

    private EditText newName, newBirth;
    private Button save, changePsw, changeEmail;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatePickerDialog.OnDateSetListener dateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        newName = findViewById(R.id.etNameUpdate);
        newBirth = findViewById(R.id.etBirthUpdate);
        save = (Button) findViewById(R.id.btnSave);
        changePsw = (Button) findViewById((R.id.btnChangePsw));
        changeEmail = (Button) findViewById((R.id.btnChangeEmail));


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);

                try {
                    newName.setText(userData.getUsrName());
                    newBirth.setText(userData.getBirthDay());

                } catch (Exception ex){
                    Toast.makeText(UpdateProfile.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        newBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(UpdateProfile.this,
                        dateSetListener, year, month, day);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = year + getString(R.string.DATE_FORMAT) + determineMonth(month) + day
                        + getString(R.string.WORD_DAY);
                newBirth.setText(date);
            }
        };

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (noEmptyFields()) {
                    String name = newName.getText().toString();
                    String birth = newBirth.getText().toString();

                    UserData userData = new UserData(name, birth);

                    databaseReference.setValue(userData);

                    finish();
                }
            }
        });

        changePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateProfile.this, UpdatePassword.class));
            }
        });
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateProfile.this, UpdateEmail.class));
            }
        });
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

    private boolean noEmptyFields(){
        String pass = newName.getText().toString();
        String passRepeat = newBirth.getText().toString();
        if (pass.isEmpty() || passRepeat.isEmpty()) {
            Toast.makeText(this, "Nenurodyti kaikurie laukai", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
