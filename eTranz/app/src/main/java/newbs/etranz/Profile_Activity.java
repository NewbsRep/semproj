package newbs.etranz;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Activity extends AppCompatActivity {

    public ImageButton btn;

    private TextView name, rate, birth;
    private TextView email;
    private CircleImageView photo;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private ImageView profilePic;
    private static int PICK_IMAGE = 123;
    private Uri imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        return super.onKeyDown(keyCode, event);
    }

    private void initializeViews() {
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        email = (TextView) findViewById(R.id.tvEmail);
        email.setText(firebaseAuth.getCurrentUser().getEmail());
        email.setVisibility(View.VISIBLE);

        name = findViewById(R.id.tvName);
        rate = findViewById(R.id.tvRate);
        birth = findViewById(R.id.tvBirth);

        storageReference = firebaseStorage.getReference().child(firebaseAuth.getUid()).child("Images").child("ProfilePic");
        databaseReference = firebaseDatabase.getReference().child("users").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);

                try {
                    name.setText(userData.getUsrName());
                    name.setVisibility(View.VISIBLE);

                    rate.setText(String.valueOf(userData.getRating()));
                    rate.setVisibility(View.VISIBLE);

                    birth.setText(userData.getBirthDay());
                    birth.setVisibility(View.VISIBLE);

                    photo = findViewById(R.id.civPhoto);
                    Glide.with(Profile_Activity.this)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .error(R.drawable.profile_img)
                            .fitCenter()
                            .into(photo);
                } catch (Exception ex){
                    Toast.makeText(Profile_Activity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        btn = (ImageButton) findViewById(R.id.ibEdit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile_Activity.this, UpdateProfile.class));
            }
        });
    }

}
