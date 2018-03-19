package newbs.etranz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeScreen_Activity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    FirebaseAuth firebaseObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_);
        initializeObj();
        checkUserStatus();
        loggedInAs();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_account:
                        goToProfile();
                        return true;
                    case R.id.nav_settings:
                        goToSettings();
                        return true;
                    case R.id.nav_logout:
                        logOut();
                        return true;
                }

                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void loggedInAs(){
        FirebaseUser usr = firebaseObj.getCurrentUser();

        if(usr != null){
            String email = firebaseObj.getCurrentUser().getEmail().trim();
            Toast.makeText(HomeScreen_Activity.this, "Prisijungta kaip " + email.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void initializeObj() {
        firebaseObj = firebaseObj.getInstance();
    }

    public void goToProfile(){
        startActivity(new Intent(HomeScreen_Activity.this, Profile_Activity.class));
    }

    public void goToSettings(){
        startActivity(new Intent(HomeScreen_Activity.this, Settings_Activity.class));
    }

    public void checkUserStatus(){
        FirebaseUser usr = firebaseObj.getCurrentUser();
        if(usr == null){
            finish();
            startActivity(new Intent(HomeScreen_Activity.this, Login_Activity.class));
        }
    }

    public void logOut(){
        finish();
        firebaseObj.signOut();
        Toast.makeText(HomeScreen_Activity.this, "Sėkmingai atsijungta", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(HomeScreen_Activity.this, Login_Activity.class));
    }
}
