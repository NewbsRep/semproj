package newbs.etranz;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeScreen_Activity extends AppCompatActivity {
    private TextView tv_PostATrip;
    private TextView tv_SearchForATrip;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private TextView tv_ProfileName;
    private CircleImageView civ_ProfilePicture;
  
    private boolean isSignedIn = true;
  
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeObj();
        checkUserStatus();
        if(isSignedIn) setUpProfileSettings();
        //loggedInAs();

        //printVersion();

        tv_PostATrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen_Activity.this, New_Trip_Activity.class));
            }
        });

        tv_SearchForATrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen_Activity.this, TripSearch_Activity.class));
            }
        });

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
                    case R.id.nav_myTrips:
                        goToMyTrips();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void loggedInAs(){
        FirebaseUser usr = firebaseAuth.getCurrentUser();
        if(usr != null){
            String email = firebaseAuth.getCurrentUser().getEmail().trim();
            Toast.makeText(HomeScreen_Activity.this, "Prisijungta kaip " + email.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void initializeObj() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        tv_PostATrip = findViewById(R.id.tv_PostATrip);
        tv_SearchForATrip = findViewById(R.id.tv_SearchForATrip);
    }

    public void checkUserStatus(){
        FirebaseUser usr = firebaseAuth.getCurrentUser();
        if(usr == null){
            isSignedIn = false;
            finish();
            startActivity(new Intent(HomeScreen_Activity.this, Login_Activity.class));
        }
        else if(!usr.isEmailVerified()){
            isSignedIn = false;
            finish();
            firebaseAuth.signOut();
            startActivity(new Intent(HomeScreen_Activity.this, Login_Activity.class));
        }
    }

    public void setUpProfileSettings(){
        databaseReference = firebaseDatabase.getReference().child("users").child(firebaseAuth.getUid());
        storageReference = firebaseStorage.getReference().child(firebaseAuth.getUid()).child("Images").child("ProfilePic");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);

                try {
                    tv_ProfileName = findViewById(R.id.tv_ProfileName);
                    tv_ProfileName.setText(userData.getUsrName());

                    if (Build.VERSION.SDK_INT >= 26)
                        tv_ProfileName.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);

                    civ_ProfilePicture = findViewById(R.id.civ_ProfilePicture);
                    Glide.with(HomeScreen_Activity.this)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .error(R.drawable.profile_img)
                            .fitCenter()
                            .into(civ_ProfilePicture);
                } catch (Exception ex){
                    Toast.makeText(HomeScreen_Activity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                }
        });
    }

    public void printVersion(){
        int version = Build.VERSION.SDK_INT;
        Toast.makeText(HomeScreen_Activity.this, "SDK versija: " + version, Toast.LENGTH_SHORT).show();
    }

    public void goToProfile(){
        startActivity(new Intent(HomeScreen_Activity.this, Profile_Activity.class));
    }

    public void goToMyTrips(){
        startActivity(new Intent(HomeScreen_Activity.this, MyTrips_Activity.class));
    }

    public void goToSettings(){
        startActivity(new Intent(HomeScreen_Activity.this, Settings_Activity.class));
    }

    public void logOut(){
        isSignedIn = false;
        finish();
        firebaseAuth.signOut();
        Toast.makeText(HomeScreen_Activity.this, "Sėkmingai atsijungta", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(HomeScreen_Activity.this, Login_Activity.class));
    }
}
