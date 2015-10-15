package uk.me.jamesharris.buses;


import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by james on 01/05/15.
 */
public class BusListFragment extends BusDetailFragmentBase {
    protected static final String TAG = "BusListFragment";
    private static final String ARG_ID = "STOP_ID";
    private static final String ARG_STOP_CODE = "STOP_CODE";

    private StopSpec mStopSpec;
    @Override
    protected ArrayList<BusStop> fetchBuses() throws IOException {
        return mStopSpec.loadBuses();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();


        if (args!=null) {
            int id = args.getInt(ARG_ID, -1);
            if (id >= 0) {
                mStopSpec = BusManager.get(getActivity()).getStopSpec(id);
            } else {
                String code = args.getString(ARG_STOP_CODE);
                if (!code.isEmpty()) {
                    mStopSpec = new StopSpec();
                    mStopSpec.setDescription("new");
                    mStopSpec.addStop(code);
                }
            }
        }
        if (mStopSpec==null) {
            mStopSpec = StopSpec.getDefault();
        }
        Log.i(TAG, "created BusListFragment: stopspec " + mStopSpec.getDescription());


    }

    @Override
    protected String getFragmentTag() {
        return TAG;
    }
    public static BusListFragment newInstance(int stopSpec) {
        Bundle args = new Bundle();
        args.putInt(ARG_ID, stopSpec);
        BusListFragment rf = new BusListFragment();
        rf.setArguments(args);
        return rf;
    }
    public static BusListFragment newInstanceAlt(String stopCode) {
        Bundle args = new Bundle();
        args.putString(ARG_STOP_CODE, stopCode);
        BusListFragment rf = new BusListFragment();
        rf.setArguments(args);
        return rf;
    }


}
