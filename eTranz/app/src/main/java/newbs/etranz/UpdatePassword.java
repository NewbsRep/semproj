package newbs.etranz;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePassword extends AppCompatActivity {

    private Button update;
    private EditText newPsw, newPswRepeat, oldPass;
    private FirebaseUser firebaseUser;
    private FirebaseApp firebaseApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        update = findViewById(R.id.btnUpdatePsw);
        newPsw = findViewById(R.id.etNewPsw);
        newPswRepeat = findViewById(R.id.etNewPswRepeat);
        oldPass = findViewById(R.id.etOldPass);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noEmptyFields() && passwordMatches()) {

                    final String userPswNew = newPsw.getText().toString();
                    final String oldPassw  = oldPass.getText().toString();


                    final FirebaseUser fireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(fireBaseUser.getEmail(), oldPassw);
                    fireBaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> reAuthenticateTask) {
                            if (!reAuthenticateTask.isSuccessful()) {
                                oldPass.setError("Neteisingas slaptažodis");
                                oldPass.requestFocus();
                                return;
                            }
                            else{
                                firebaseUser.updatePassword(userPswNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UpdatePassword.this, "Slaptažodis Pakeistas", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            try {
                                                throw task.getException();
                                            } catch(FirebaseAuthWeakPasswordException e) {
                                                newPsw.setError(getString(R.string.weakPass));
                                                newPsw.requestFocus();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(UpdatePassword.this, "Bandykite dar kartą", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                            }


                        }
                    });
                }

            }
        });

    }

    private boolean noEmptyFields(){
        if (newPsw.getText().toString().isEmpty()){
            newPsw.setError(getResources().getString(R.string.emptyFieldMsg));
            newPsw.requestFocus();
            return false;
        }
        else if(newPswRepeat.getText().toString().isEmpty()){
            newPswRepeat.setError(getResources().getString(R.string.emptyFieldMsg));
            newPswRepeat.requestFocus();
            return false;
        }
        else if(oldPass.getText().toString().isEmpty()){
            oldPass.setError(getResources().getString(R.string.emptyFieldMsg));
            oldPass.requestFocus();
            return false;
        }
        else {
            return true;
        }
    }

    private boolean passwordMatches() {
        String pass = newPsw.getText().toString();
        String passRepeat = newPswRepeat.getText().toString();
        if(passRepeat.isEmpty()) {
            Toast.makeText(this, getString(R.string.passwordRepeat), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pass.equals(passRepeat))
            return true;
        Toast.makeText(this, getString(R.string.PASSWORD_REPEAT_INCORRECT), Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
