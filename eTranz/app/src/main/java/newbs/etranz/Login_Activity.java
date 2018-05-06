package newbs.etranz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {
    private EditText etMail, etPassword;
    private Button btnLogin;
    private TextView tvRegister, tvPassRemind;
    private FirebaseAuth firebaseObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        initializeObj();

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this ,registerActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUser(etMail.getText().toString(), etPassword.getText().toString());
            }
        });

        tvPassRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, PassReset_Activity.class));
            }
        });

    }

    public boolean checkForEmptyFields(){
        if(etMail.getText().toString().isEmpty()){
            etMail.setError(getResources().getString(R.string.emptyFieldMsg));
            etMail.requestFocus();
            return true;
        }
        else if(etPassword.getText().toString().isEmpty()){
            etPassword.setError(getResources().getString(R.string.emptyFieldMsg));
            etPassword.requestFocus();
            return true;
        }
        else {
            return false;
        }
    }


    public void validateUser(String mail, String password){
        if(!checkForEmptyFields()) {
            final ProgressDialog pd = new ProgressDialog(Login_Activity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Vyksta prisijungimas");
            pd.show();

            firebaseObj.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        pd.dismiss();
                        isEmailVerified();
                    } else {
                        boolean connected = false;
                        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
                            connected = true;
                        }

                        if (connected == true){
                            Toast.makeText(Login_Activity.this, "Neteisingi prisijungimo duomenys", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login_Activity.this, "Nėra interneto ryšio", Toast.LENGTH_SHORT).show();
                        }
                        
                        pd.cancel();
                    }
                }
            });
        }
    }


    public void initializeObj(){
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        firebaseObj = FirebaseAuth.getInstance();
        tvPassRemind = findViewById(R.id.tvPassRemind);
    }

    protected void isEmailVerified(){
        FirebaseUser user = firebaseObj.getCurrentUser();
        if(user.isEmailVerified()){
            Toast.makeText(Login_Activity.this, "Prisijungimas sėkmingas", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(Login_Activity.this, HomeScreen_Activity.class));
        }
        else{
            AlertDialog();
        }
    }

    private void AlertDialog(){
        new AlertDialog.Builder(this)
                .setTitle("El. pašto adreso patvirtinimas")
                .setMessage("Patvirtinkite el. pašto adresą, sekdami laiške gautomis instrukciijomis.")
                .setNegativeButton("Negavau el. laiško", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendEmailVerification();
                        firebaseObj.signOut();
                    }
                })
                .setPositiveButton("Gerai", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseObj.signOut();
                    }
                }).create().show();
    }

    private void  sendEmailVerification(){
        FirebaseUser user = firebaseObj.getCurrentUser();
        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Login_Activity.this, "Išsiųstas el. pašto patvirtinimo laiškas", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Login_Activity.this, "Nepavyko išsiųsti el. pašto patvirtino laiško", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
