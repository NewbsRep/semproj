package newbs.etranz;

import android.app.ProgressDialog;
import android.content.Context;
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
    private EditText etMail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private FirebaseAuth firebaseObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeObj();

        if (Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.logo_default));
        }

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

    }

    public boolean checkForEmptyFields(){
        if(etMail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Užpildykite visus laukelius", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Login_Activity.this, "Prisijungimas sėkmingas", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(Login_Activity.this, HomeScreen_Activity.class));
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
    }

}
