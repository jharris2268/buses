package uk.me.jamesharris.buses;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by james on 21/06/15.
 */
class BusListAdapter extends BaseAdapter {

    private Activity mActivity;

    private class pair {
        pair(int s, int b) {
            stop = s;
            bus = b;
        }

        int stop;
        int bus;
    }

    ArrayList<BusStop> mItems;

    ArrayList<pair> mIdx;

    public BusListAdapter(Activity activity, ArrayList<BusStop> items) {
        mActivity = activity;
        mItems = items;
        mIdx = new ArrayList<>();
        String stopCode="";
        int i = 0;
        for (BusStop bus : items)  {
            if (!stopCode.equals(bus.getStopId())) {
                mIdx.add(new pair(i, -1));
                stopCode=bus.getStopId();
            }
            mIdx.add(new pair(i, 0));
            i++;
            /*for (int j=0; j < mItems.get(i).mExpectedBuses.size(); j++) {
                mIdx.add(new pair(i,j));
            }*/
        }

    }


    @Override
    public int getCount() {
        return mIdx.size();
    }

    @Override
    public Object getItem(int position) {
        pair p = mIdx.get(position);
        //Log.i(TAG,position+" => "+p.stop+", "+p.bus);
        BusStop c = mItems.get(p.stop);
        if (p.bus == -1) {
            return c;
        }
        //return c.mExpectedBuses.get(p.bus);
        return c.getExpectedBus();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        pair p = mIdx.get(position);
        if (p.bus == -1) {
            return 0;
        }
        return 1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Object o = getItem(position);
        //Log.i(TAG,"getView @ "+position+" "+o);
        if (o instanceof BusStop) {
            return getStopView((BusStop) o, view, parent);
        }
        return getBusView((ExpectedBus) o, view, parent);
    }

    private View getStopView(BusStop c, View view, ViewGroup parent) {
        if (view == null) {
            view = mActivity.getLayoutInflater()
                    .inflate(R.layout.bus_stop_detail_item, null);
        }

        TextView titleTextView =
                (TextView) view.findViewById(R.id.bus_stop_detail_title);
        titleTextView.setText(c.getStopPointName());

        TextView indicatorTextView =
                (TextView) view.findViewById(R.id.bus_stop_detail_indicator);
        if (c.getStopPointIndicator().equals("null")) {
            indicatorTextView.setText("");
        } else {
            indicatorTextView.setText("[" + c.getStopPointIndicator() + "]");
        }
        TextView destTextView =
                (TextView) view.findViewById(R.id.bus_stop_detail_destination);
        if (c.getTowards().equals("null")) {
            destTextView.setText("");
        } else {
            destTextView.setText("to " + c.getTowards());
        }
        return view;
    }

    public View getBusView(ExpectedBus bus, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater()
                    .inflate(R.layout.expected_bus_detail_item, null);
        }

        TextView timeToTextView =
                (TextView) convertView.findViewById(R.id.expected_bus_detail_time_to);
        timeToTextView.setText(bus.getTimeTo());

        TextView routeTextView = (TextView) convertView.findViewById(R.id.expected_bus_detail_route);
        routeTextView.setText(bus.getLineName());
        TextView destinationTextView = (TextView) convertView.findViewById(R.id.expected_bus_detail_destination);
        destinationTextView.setText(bus.getDestinationName());
        TextView expectedTextView = (TextView) convertView.findViewById(R.id.expected_bus_detail_expected_time);
        expectedTextView.setText(bus.getEstimatedTime().toString());
        return convertView;
    }


}
