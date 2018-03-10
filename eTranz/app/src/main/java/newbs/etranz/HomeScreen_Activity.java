package newbs.etranz;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeScreen_Activity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    FirebaseAuth firebaseObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_);
        initializeObj();
        loggedInAs();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void accountSettingsBP(View view){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }


    public void loggedInAs(){
        FirebaseUser usr = firebaseObj.getCurrentUser();

        if(usr != null){
            String email = firebaseObj.getCurrentUser().getEmail().trim();
            Toast.makeText(HomeScreen_Activity.this, "Prisijungta kaip " + email.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void initializeObj(){
        firebaseObj = firebaseObj.getInstance();
    }


    public void logOut(){
    }
}
