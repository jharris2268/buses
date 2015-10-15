package uk.me.jamesharris.buses;

import android.os.Bundle;
import android.support.v4.app.Fragment;



public class BusDetailActivity extends SingleFragmentActivity {

    public static final String EXTRA_STOPCODE =
            "uk.me.jamesharris.buses.stop_code";
    public static final String EXTRA_REGISTRATIONNUMBER =
            "uk.me.jamesharris.buses.registration_number";
    public static final String EXTRA_PREVIOUSTYPE =
            "uk.me.jamesharris.buses.previous_type";


    @Override
    public Fragment createFragment() {

        String registrationNumber = getIntent().getStringExtra(EXTRA_REGISTRATIONNUMBER);
        String stopCode = getIntent().getStringExtra(EXTRA_STOPCODE);
        String previousType = getIntent().getStringExtra(EXTRA_PREVIOUSTYPE);

        if (!stopCode.equals("")) {
            if (previousType.equals("BusDetail")) {
                return BusListFragment.newInstanceAlt(stopCode);
            }
            return BusDetailFragment.newInstance(registrationNumber, stopCode);
        }
        return new BusDetailFragment();
    }

    @Override
    public void onCreate(Bundle sis) {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(sis);
    }
}
