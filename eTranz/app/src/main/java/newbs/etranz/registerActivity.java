package newbs.etranz;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

public class registerActivity extends AppCompatActivity {

    private EditText eMail, name, dateOfBirth, password, passwordRepeat;
    private Button btnRegister;
    private TextView hasAcc;
    private FirebaseAuth firebaseAuth;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		initializeViews();

        hasAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registerActivity.this, Login_Activity.class));
            }
        });

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(registerActivity.this,
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
                dateOfBirth.setText(date);
            }
        };

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noEmptyFields() && passwordMatches()) {
                    String mail = eMail.getText().toString().trim();
                    String pass = password.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                finish();
                                startActivity(new Intent(registerActivity.this, HomeScreen_Activity.class));
                                Toast.makeText(registerActivity.this, getString(R.string.REGISTRATION_SUCCESS), Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(registerActivity.this, getString(R.string.MISTAKE_OCCURRED), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
        String mail = eMail.getText().toString();
        String usrName = name.getText().toString();
        String bDay = dateOfBirth.getText().toString();
        String paswd = password.getText().toString();
            if (mail.isEmpty()) {
                Toast.makeText(this, "Nenurodytas el. paštas", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (usrName.isEmpty()) {
                Toast.makeText(this, "Nenurodytas vardas", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (bDay.isEmpty()) {
                Toast.makeText(this, "Nenurodyta gimimo data", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (paswd.isEmpty()) {
                Toast.makeText(this, "Nenurodytas slaptažodis", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
    }

    private boolean passwordMatches() {
        String pass = password.getText().toString();
        String passRepeat = passwordRepeat.getText().toString();
        if(passRepeat.isEmpty()) {
            Toast.makeText(this, getString(R.string.passwordRepeat), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pass.equals(passRepeat))
            return true;
        Toast.makeText(this, getString(R.string.PASSWORD_REPEAT_INCORRECT), Toast.LENGTH_SHORT).show();
        return false;
    }

    private void initializeViews(){
		setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        eMail = (EditText) findViewById(R.id.etEmail);
        name = (EditText) findViewById(R.id.etName);
        dateOfBirth = (EditText) findViewById(R.id.etBirthDate);
        password = (EditText) findViewById(R.id.etPassword);
        passwordRepeat = (EditText) findViewById(R.id.etPasswordRepeat);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        hasAcc = (TextView) findViewById(R.id.tvHaveAcc);
    }
}
