package newbs.etranz;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;

import java.util.Calendar;

public class registerActivity extends AppCompatActivity {

    private EditText eMail, name, dateOfBirth, password, passwordRepeat, phone;
    private Button btnRegister;
    private TextView hasAcc;
    private FirebaseAuth firebaseAuth;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private ImageView profilePic;
    private static int PICK_IMAGE = 123;
    private Uri imagePath;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Registracija");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeObj();

        hasAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                dateOfBirth.setError(null);
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

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Pasirinkite profilio nuotrauką"), PICK_IMAGE);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noEmptyFields() && passwordMatches()) {
                    String mail = eMail.getText().toString().trim();
                    String pass = password.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                uploadUsrData();
                                sendEmailVerification();
                            } else {
                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthWeakPasswordException e) {
                                    password.setError(getString(R.string.weakPass));
                                    password.requestFocus();
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    eMail.setError(getString(R.string.wrongMailFormat));
                                    eMail.requestFocus();
                                } catch(FirebaseAuthUserCollisionException e) {
                                    eMail.setError(getString(R.string.userExists));
                                    eMail.requestFocus();
                                } catch(Exception e) {
                                    Toast.makeText(registerActivity.this, getString(R.string.MISTAKE_OCCURRED), Toast.LENGTH_SHORT).show();
                                }
                            }
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
            if (eMail.getText().toString().isEmpty()) {
                eMail.setError(getResources().getString(R.string.emptyFieldMsg));
                eMail.requestFocus();
                return false;
            }
            if (name.getText().toString().isEmpty()) {
                name.setError(getResources().getString(R.string.emptyFieldMsg));
                name.requestFocus();
                return false;
            }
            if (dateOfBirth.getText().toString().isEmpty()) {
                dateOfBirth.setError(getResources().getString(R.string.emptyFieldMsg));
                dateOfBirth.requestFocus();
                return false;
            }
            if (password.getText().toString().isEmpty()) {
                password.setError(getResources().getString(R.string.emptyFieldMsg));
                password.requestFocus();
                return false;
            }
             if (passwordRepeat.getText().toString().isEmpty()) {
                 passwordRepeat.setError(getResources().getString(R.string.emptyFieldMsg));
                 passwordRepeat.requestFocus();
                 return false;
            }
            if (phone.getText().toString().isEmpty()) {
                phone.setError(getResources().getString(R.string.emptyFieldMsg));
                phone.requestFocus();
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

    private void initializeObj(){
        firebaseAuth = FirebaseAuth.getInstance();
        eMail = (EditText) findViewById(R.id.etEmailPsw);
        name = (EditText) findViewById(R.id.etName);
        dateOfBirth = (EditText) findViewById(R.id.etBirthDate);
        dateOfBirth.setInputType(EditorInfo.TYPE_NULL);
        password = (EditText) findViewById(R.id.etPassword);
        passwordRepeat = (EditText) findViewById(R.id.etPasswordRepeat);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        hasAcc = (TextView) findViewById(R.id.tvHaveAcc);
        profilePic = (ImageView) findViewById(R.id.profile_image);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        phone = findViewById(R.id.etPhone);
    }

    private void uploadUsrData() {
        if(imagePath != null && !imagePath.equals(Uri.EMPTY)){
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference usrStorage = storageReference.child(firebaseAuth.getUid()).child("Images").child("ProfilePic");
            UploadTask uploadTask = usrStorage.putFile(imagePath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(registerActivity.this, "Serverio klaida", Toast.LENGTH_SHORT).show();
                }
            });
        }
        DatabaseReference dbRef = firebaseDatabase.getReference();
        String usrName = name.getText().toString();
        String bDay = dateOfBirth.getText().toString();
        String phoneNum = phone.getText().toString();
        UserData userData = new UserData(usrName, bDay, phoneNum);
        dbRef.child("users").child(firebaseAuth.getUid()).setValue(userData).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(registerActivity.this, "Duomenų bazės klaida", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void  sendEmailVerification(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(registerActivity.this, "Registracija sėkminga," +
                                " išsiųstas el. pašto patvirtinimo laiškas", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(registerActivity.this, HomeScreen_Activity.class));
                    }
                    else{
                        Toast.makeText(registerActivity.this, "Nepavyko išsiųsti el. pašto patvirtino laiško", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
