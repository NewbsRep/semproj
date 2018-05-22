package newbs.etranz;

import android.view.MenuItem;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class Settings_ActivityTest {

    private Settings_Activity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(Settings_Activity.class)
                .create()
                .get();
    }

    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }
}