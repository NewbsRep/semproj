package newbs.etranz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile_Activity extends AppCompatActivity {

    private TextView name;
    private TextView email;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*Button logoutBtn = (Button) findViewById(R.id.btnLogout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_login);
            }
        });*/
        //getActionBar().setTitle("Profilis");
    }
  
    private void initializeViews() {
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();

        name = (TextView) findViewById(R.id.tvName) ;
        name.setText(firebaseAuth.getCurrentUser().getDisplayName());
        name.setVisibility(View.VISIBLE);

        email = (TextView) findViewById(R.id.tvEmail);
        email.setText(firebaseAuth.getCurrentUser().getEmail());
        email.setVisibility(View.VISIBLE);
    }

    public void logoutButtonPressed(View view){
        Intent intent = new Intent(this, Login_Activity.class);
        startActivity(intent);
    }
}
