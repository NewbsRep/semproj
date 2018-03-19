package newbs.etranz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class registerActivity extends AppCompatActivity {

    private EditText eMail, name, dateOfBirth, password, passwordRepeat;
    private Button btnRegister;
    private TextView hasAcc;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ImageView profilePic;
    private DatePicker datePicker;

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

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noEmptyFields() == true) {
                    String mail = eMail.getText().toString().trim();
                    String pass = password.getText().toString().trim();

                    /*
                    String passRepeat = passwordRepeat.getText().toString().trim();
                    if (pass != passRepeat){
                        Toast.makeText(registerActivity.this, "Slaptažodžiai nesutampa", Toast.LENGTH_SHORT).show();
                        onCreate(savedInstanceState);
                    }
                    */
                    firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                uploadUsrData();
                                startActivity(new Intent(registerActivity.this, HomeScreen_Activity.class));
                                Toast.makeText(registerActivity.this, "Registracija sėkminga", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(registerActivity.this, "Įvyko klaida", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean noEmptyFields(){
        String mail = eMail.getText().toString();
        String usrName = name.getText().toString();
        String bDay = dateOfBirth.getText().toString();
        String paswd = password.getText().toString();

        if(mail.isEmpty() || usrName.isEmpty() || bDay.isEmpty() || paswd.isEmpty()) {
            Toast.makeText(this, "Prašome užpildyti visus laukus", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
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
        profilePic = (ImageView) findViewById(R.id.ivProfilePic);
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    private void uploadUsrData()
    {
        DatabaseReference dbRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        String usrName = name.getText().toString();
        int bDay = Integer.parseInt(dateOfBirth.getText().toString());
        UserData userData = new UserData(usrName, bDay);
        dbRef.setValue(userData);
    }
}
