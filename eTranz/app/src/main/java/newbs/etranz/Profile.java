package newbs.etranz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*Button logoutBtn = (Button) findViewById(R.id.btnLogout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_login);
            }
        });*/
        //getActionBar().setTitle("Profilis");
    }

    public void logoutButtonPressed(View view){
        Intent intent = new Intent(this, Login_Activity.class);
        startActivity(intent);
    }
}
