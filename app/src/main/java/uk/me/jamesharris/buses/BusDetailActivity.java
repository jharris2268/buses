package uk.me.jamesharris.buses;

import android.os.Bundle;
import android.support.v4.app.Fragment;



public class BusDetailActivity extends SingleFragmentActivity {
    /*List of stops for one bus*/


    public static final String EXTRA_STOPCODE =
            "uk.me.jamesharris.buses.stop_code";
    public static final String EXTRA_REGISTRATIONNUMBER =
            "uk.me.jamesharris.buses.registration_number";


    @Override
    public Fragment createFragment() {

        String registrationNumber = getIntent().getStringExtra(EXTRA_REGISTRATIONNUMBER);
        String stopCode = getIntent().getStringExtra(EXTRA_STOPCODE);

        if (!stopCode.equals("")) {
            return BusDetailFragment.newInstance(registrationNumber, stopCode);
        }
        return new BusDetailFragment();
    }

    @Override
    public void onCreate(Bundle sis) {
        super.onCreate(sis);
    }
}
