package newbs.etranz;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePassword extends AppCompatActivity {

    private Button update;
    private EditText newPsw, newPswRepeat;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        update = findViewById(R.id.btnUpdatePsw);
        newPsw = findViewById(R.id.etNewPsw);
        newPswRepeat = findViewById(R.id.etNewPswRepeat);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noEmptyFields() && passwordMatches()) {

                    String userPswNew = newPsw.getText().toString();

                    firebaseUser.updatePassword(userPswNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UpdatePassword.this, "Slaptažodis Pakeistas", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(UpdatePassword.this, "Slaptažodžio Pakeisti Nepavyko", Toast.LENGTH_SHORT).show();
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
}
