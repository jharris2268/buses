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
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by james on 01/05/15.
 */
public abstract class BusDetailFragmentBase extends Fragment {
    protected static String TAG = "BusDetailFragmentBase";


    protected boolean resetIfLeft = false;

    ListView mListView;
    Button mUpdateButton;
    ListAdapter mListAdapter;

    ArrayList<BusStop> mItems;
    private Timer mTimer;
    private Date mPausedTime;


    protected abstract ArrayList<BusStop> fetchBuses() throws IOException;
    protected abstract String getFragmentTag();

    private class FetchItemsTask extends AsyncTask<Void,Void,ArrayList<BusStop>> {

        @Override
        protected ArrayList<BusStop> doInBackground(Void... params) {
            try {
                return fetchBuses();
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

        //resetTimer();
    }

    public void onPause() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mPausedTime = new Date();
        Log.i(TAG,"paused at "+mPausedTime);
        super.onPause();
    }
    public void onResume() {
        super.onResume();
        Log.i(TAG,"resumed");
        if (mPausedTime != null && resetIfLeft) {
            Date now = new Date();
            long diffInMs = now.getTime() - mPausedTime.getTime();
            long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
            if (diffInSec > 1*60) {
                Log.i(TAG,"resumed after "+diffInSec+" seconds; restart");
                Intent intent = new Intent(getActivity(), StopListActivity.class);
                SingleFragmentActivity par = (SingleFragmentActivity) (getActivity());
                if (par != null) {
                    par.fragmentCancel();
                }
                startActivity(intent);
                return;
            }
        }
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



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_buses,viewGroup,false);


        mListView = (ListView) v.findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mListAdapter==null) { return; }
                Object obj = mListAdapter.getItem(position);
                if ((obj instanceof ExpectedBus)) {

                    ExpectedBus bus = (ExpectedBus) obj;
                    Intent i;
                    /*pick other activity*/
                    if (getFragmentTag().equals(BusListFragment.TAG)) {
                        i = new Intent(getActivity(), BusDetailActivity.class);
                    } else {
                        i = new Intent(getActivity(), BusListActivity.class);
                    }

                    i.putExtra(BusDetailActivity.EXTRA_STOPCODE, bus.getStopId());
                    i.putExtra(BusDetailActivity.EXTRA_REGISTRATIONNUMBER, bus.getRegistrationNumber());

                    startActivity(i);
                } else if (obj instanceof BusStop) {

                    /*create location intent understood by maps app*/

                    BusStop busStop = (BusStop) obj;
                    StringBuilder sb = new StringBuilder();
                    sb.append("geo:").append(busStop.getLatitude());
                    sb.append(",").append(busStop.getLongitude());
                    sb.append("?q=").append(busStop.getLatitude());
                    sb.append(",").append(busStop.getLongitude());
                    sb.append("(").append(Uri.encode(busStop.getStopPointName())).append(")");
                    Log.i(TAG,"start map "+sb.toString());
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
        setupAdapter();

        return v;
    }


    private void setupAdapter() {
        if (getActivity() == null) {
            return;
        }
        if (mItems != null) {
            Log.i(TAG, "setupAdapter: have " + mItems.size()+" buses");
            mListAdapter = new BusListAdapter(getActivity(), mItems);
            mListView.setAdapter(mListAdapter);

        } else {
            mListAdapter = null;
            mListView.setAdapter(mListAdapter);

        }
        mUpdateButton.setText(timeStr() + " " + "UPDATE");
    }
    public void updateItems() {
        new FetchItemsTask().execute();
    }

    private String timeStr() {
        Date now = new Date();
        return (String) DateFormat.format("HH:mm:ss",now);
    }

}

