package newbs.etranz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MyTrips_Passenger_FragmentTest {
    private MyTrips_Passenger_Fragment fragment = new MyTrips_Passenger_Fragment();

    @Test
    public void checkFragmentNotNull() throws Exception {
        startFragment(fragment);
        assertNotNull(fragment);
    }
}