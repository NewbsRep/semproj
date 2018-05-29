package newbs.etranz;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmail extends AppCompatActivity {

    private Button update;
    private EditText newEmail;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        update = findViewById(R.id.btnUpdateEmail);
        newEmail = findViewById(R.id.etNewEmail);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmailNew = newEmail.getText().toString();
                if (noEmptyFields()) {

                firebaseUser.updateEmail(userEmailNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateEmail.this, "El. Paštas Pakeistas", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                newEmail.setError(getString(R.string.wrongMailFormat));
                                newEmail.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e) {
                                newEmail.setError(getString(R.string.userExists));
                                newEmail.requestFocus();
                            } catch(Exception e) {
                                Log.d("klaida", e.toString());
                                Toast.makeText(UpdateEmail.this, getString(R.string.MISTAKE_OCCURRED), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private boolean noEmptyFields(){
        String email = newEmail.getText().toString();
        if (email.isEmpty()) {
            newEmail.setError(getResources().getString(R.string.emptyFieldMsg));
            newEmail.requestFocus();
            return false;
        }
        return true;
    }
}
