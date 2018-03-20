package newbs.etranz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;

public class registerActivity extends AppCompatActivity {

    private EditText eMail, name, dateOfBirth, password, passwordRepeat;
    private Button btnRegister;
    private TextView hasAcc;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private ImageView profilePic;
    private DatePicker datePicker;
    private static int PICK_IMAGE = 123;
    Uri imagePath;

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
		initializeObj();
        hasAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registerActivity.this, Login_Activity.class));
            }
        });

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
                                Toast.makeText(registerActivity.this, "Registracija sėkminga", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(registerActivity.this, HomeScreen_Activity.class));
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

    private void initializeObj(){
        firebaseAuth = FirebaseAuth.getInstance();
        eMail = (EditText) findViewById(R.id.etEmail);
        name = (EditText) findViewById(R.id.etName);
        dateOfBirth = (EditText) findViewById(R.id.etBirthDate);
        password = (EditText) findViewById(R.id.etPassword);
        passwordRepeat = (EditText) findViewById(R.id.etPasswordRepeat);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        hasAcc = (TextView) findViewById(R.id.tvHaveAcc);
        profilePic = (ImageView) findViewById(R.id.profile_image);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    private void uploadUsrData()
    {
        if(!imagePath.equals(null)){
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
        DatabaseReference dbRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        String usrName = name.getText().toString();
        int bDay = Integer.parseInt(dateOfBirth.getText().toString());
        UserData userData = new UserData(usrName, bDay);
        dbRef.setValue(userData).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(registerActivity.this, "Duomenų bazės klaida", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
