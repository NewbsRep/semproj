package newbs.etranz;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import android.support.v4.app.Fragment;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MyTrips_Driver_FragmentTest {

    private MyTrips_Driver_Fragment fragment = new MyTrips_Driver_Fragment();

    @Test
    public void checkFragmentNotNull() throws Exception {
        startFragment(fragment);
        assertNotNull(fragment);
    }
}