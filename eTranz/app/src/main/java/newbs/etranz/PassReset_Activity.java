package newbs.etranz;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class PassReset_Activity extends AppCompatActivity {
    Button reset;
    EditText email;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_reset_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.logo_default));
        }
        reset = findViewById(R.id.btnResetPswd);
        email = findViewById(R.id.etEmailPsw);
        firebaseAuth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail  = email.getText().toString().trim();
                if(mail.isEmpty()){
                    email.setError(getResources().getString(R.string.emptyFieldMsg));
                    email.requestFocus();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PassReset_Activity.this, R.string.resetPassSuccess, Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PassReset_Activity.this, Login_Activity.class));
                            }
                            else {
                                Toast.makeText(PassReset_Activity.this,R.string.resetFail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
