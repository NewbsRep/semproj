package newbs.etranz;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class MyTrips_Activity extends AppCompatActivity {

    private static final String TAG = "MyTrips_Activity";

    private MyTrips_SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);
        Log.d(TAG, "onCreate: Starting.");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPageAdapter = new MyTrips_SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        MyTrips_SectionsPageAdapter adapter = new MyTrips_SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new MyTrips_Driver_Fragment(), "Vairuoju");
        adapter.addFragment(new MyTrips_Passenger_Fragment(), "!Vairuoju");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
