package uk.me.jamesharris.buses;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by james on 01/05/15.
 */

public class BusDetailFragment extends BusDetailsFragmentBase {
    protected static final String TAG = "BusDetailFragment";

    private static final String ARG_STOP_CODE = "STOP_CODE";
    private static final String ARG_REGISTRATION_NUMBER= "REGISTRATION_NUMBER";


    private String mRegistrationNumber;
    private String mStopCode;


    @Override
    protected ArrayList<BusStop> fetchBuses() throws IOException {
        return BusJsonLoader.loadParticularBus(mRegistrationNumber);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();


        if (args!=null) {
            mStopCode = args.getString(ARG_STOP_CODE);
            mRegistrationNumber = args.getString(ARG_REGISTRATION_NUMBER);
        }

        Log.i(TAG, "created BusDetailFragment: " + mRegistrationNumber + " @ " + mStopCode);

    }

    @Override
    protected String getFragmentType() {
        return "BusDetail";
    }

    public static BusDetailFragment newInstance(String registrationNumber, String stopCode) {
        Bundle args = new Bundle();
        args.putString(ARG_STOP_CODE, stopCode);
        args.putString(ARG_REGISTRATION_NUMBER, registrationNumber);

        BusDetailFragment rf = new BusDetailFragment();
        rf.setArguments(args);
        rf.resetIfLeft = true;
        return rf;
    }
}

/*

public class BusDetailFragment extends Fragment {
    private static final String TAG = "BusDetailFragment";
    private static final String ARG_STOP_CODE = "STOP_CODE";
    private static final String ARG_REGISTRATION_NUMBER= "REGISTRATION_NUMBER";

    ListAdapter mListAdapter;
    ListView mListView;
    Button mUpdateButton;

    private String mRegistrationNumber;
    private String mStopCode;

    ArrayList<BusStop> mItems;
    private Timer mTimer;

    private class FetchItemsTask extends AsyncTask<Void,Void,ArrayList<BusStop>> {

        @Override
        protected ArrayList<BusStop> doInBackground(Void... params) {
            try {
                return BusJsonLoader.loadParticularBus(mRegistrationNumber);
            } catch (IOException io) {
                Log.e(TAG, "buses error", io);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<BusStop> items) {
            mItems = items;
            setupAdapter();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);


        Bundle args = getArguments();


        if (args!=null) {
            mStopCode = args.getString(ARG_STOP_CODE);
            mRegistrationNumber = args.getString(ARG_REGISTRATION_NUMBER);
        }

        Log.i(TAG, "created BusDetailFragment: " + mRegistrationNumber + " @ " + mStopCode);

    }
    public void onPause() {
        mTimer.cancel();
        mTimer=null;
        super.onPause();
    }
    public void onResume() {
        super.onResume();
        resetTimer();

    }

    private void resetTimer() {
        if (mTimer!=null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateItems();
            }
        }, 0, 60000);

    }

    public static BusDetailFragment newInstance(String registrationNumber, String stopCode) {
        Bundle args = new Bundle();
        args.putString(ARG_STOP_CODE, stopCode);
        args.putString(ARG_REGISTRATION_NUMBER, registrationNumber);
        BusDetailFragment rf = new BusDetailFragment();
        rf.setArguments(args);
        return rf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        //View v = super.onCreateView(inflater,viewGroup,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_buses,viewGroup,false);
        mListView = (ListView) v.findViewById(R.id.listView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mListAdapter == null) {
                    return;
                }
                Object obj = mListAdapter.getItem(position);
                if ((obj instanceof ExpectedBus)) {

                    ExpectedBus bus = (ExpectedBus) obj;
                    Intent i = new Intent(getActivity(), BusListActivity.class);


                    i.putExtra(BusListActivity.EXTRA_STOPCODE, bus.getStopId());
                    //i.putExtra(BusDetailActivity.EXTRA_REGISTRATIONNUMBER, bus.getRegistrationNumber());
                    startActivity(i);
                } else if (obj instanceof BusStop) {
                    BusStop busStop = (BusStop) obj;
                    StringBuilder sb = new StringBuilder();
                    sb.append("geo:").append(busStop.getLatitude());
                    sb.append(",").append(busStop.getLongitude());
                    sb.append("?q=").append(busStop.getLatitude());
                    sb.append(",").append(busStop.getLongitude());
                    sb.append("(").append(Uri.encode(busStop.getStopPointName())).append(")");
                    Log.i(TAG, "start map " + sb.toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
                    startActivity(intent);

                }

            }
        });

        mUpdateButton = (Button) v.findViewById(R.id.updateButton);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });


        return v;
    }


    private void setupAdapter() {
        if (getActivity() == null) {
            return;
        }
        if (mItems != null) {
            Log.i(TAG, "setupAdapter: have " + mItems.size()+" buses");
            mListAdapter = new BusListAdapter(getActivity(), mItems);
        } else {
            mListAdapter=null;
        }
        mListView.setAdapter(mListAdapter);
        mUpdateButton.setText(timeStr() + " " + "UPDATE");
    }
    public void updateItems() {
        new FetchItemsTask().execute();
    }

    private String timeStr() {
        Date now = new Date();
        return (String) DateFormat.format("HH:mm:ss", now);
    }






}
*/