package newbs.etranz;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TripListAdapter extends BaseAdapter{

    private Context mContext;
    private List<Trip_Data> mTripList;

    //Constructor


    public TripListAdapter(Context mContext, List<Trip_Data> mTripList) {
        this.mContext = mContext;
        this.mTripList = mTripList;
    }

    @Override
    public int getCount() {
        return mTripList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTripList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.item_trip, null);

        TextView tvFromCity = (TextView)v.findViewById(R.id.tv_from_city);
        TextView tvToCity = (TextView)v.findViewById(R.id.tv_to_city);
        TextView tvPrice = (TextView)v.findViewById(R.id.tv_price);
        TextView tvDriverUID = (TextView)v.findViewById(R.id.tv_Driver);
        TextView tvFreeSpace = (TextView)v.findViewById(R.id.tv_freeSpace);
        TextView tvDepartureDate = (TextView)v.findViewById(R.id.tv_departureDate);
        TextView tvDepartureTime = (TextView)v.findViewById(R.id.tv_departureTime);

        //Set text for TextView
        tvFromCity.setText(mTripList.get(position).getFromCity());
        tvToCity.setText(mTripList.get(position).getToCity());
        tvPrice.setText("Kaina: " + mTripList.get(position).getPrice());
        tvDriverUID.setText("Vairuotojas: " + mTripList.get(position).getDriverName());
        tvFreeSpace.setText("Laisvų vietų: " + mTripList.get(position).getFreeSpace());
        tvDepartureDate.setText(mTripList.get(position).getDeparture());
        tvDepartureTime.setText(mTripList.get(position).getDepartureTime());


        //Save trip id to tag
        v.setTag(mTripList.get(position).getDriverName());

        return v;
    }
}
