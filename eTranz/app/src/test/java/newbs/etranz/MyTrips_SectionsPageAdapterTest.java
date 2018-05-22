package newbs.etranz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MyTrips_SectionsPageAdapterTest {

    @Mock
    private FragmentManager fmMock;

    private Fragment frag = mock(Fragment.class);
    private List<Fragment> mMockFragmentList = mock(ArrayList.class);
    private List<String> mMockFragmentTitileList = mock(ArrayList.class);

    private MyTrips_SectionsPageAdapter adp = new MyTrips_SectionsPageAdapter(fmMock);

    @Test
    public void addFragment() {
        adp.addFragment(frag, "so");
        assertEquals(adp.getItem(0), frag);
    }

    @Test
    public void getPageTitle() {
        adp.addFragment(frag, "so");
        assertEquals(adp.getPageTitle(0), "so");
    }

    @Test
    public void getItem() {
        adp.addFragment(frag, "so");
        assertEquals(adp.getItem(0), frag);
    }

    @Test
    public void getCount() {
        adp.addFragment(frag, "so");
        assertEquals(adp.getCount(), 1);
    }
}