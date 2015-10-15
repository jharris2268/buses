package uk.me.jamesharris.buses;


import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by james on 01/05/15.
 */

public class BusDetailFragment extends BusDetailFragmentBase {
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
    protected String getFragmentTag() {
        return TAG;
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
