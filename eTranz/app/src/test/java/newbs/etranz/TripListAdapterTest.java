package newbs.etranz;

import android.content.Context;
import org.junit.Test;
import java.util.List;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import static org.junit.Assert.*;

import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TripListAdapterTest {

    @Mock
    private Context mMockContext;
    private Trip_Data tr = new Trip_Data("Ukmerge", "Kaunas", "3", "5", "Vytauto 35", "14:30", "03");
    private List<Trip_Data> mMockTripList = mock(List.class);
    private TripListAdapter adapter = new TripListAdapter(mMockContext, mMockTripList);

    @Test
    public void getCount() {
        when(mMockTripList.size()).thenReturn(2);
        assertEquals(adapter.getCount(), 2);
    }

    @Test
    public void getItem() {
        when(mMockTripList.get(0)).thenReturn(tr);
        assertEquals(adapter.getItem(0), tr);
    }

    @Test
    public void getItemId() {
        assertEquals(adapter.getItemId(1), 1);

    }

    @Test
    public void getView() {
    }
}