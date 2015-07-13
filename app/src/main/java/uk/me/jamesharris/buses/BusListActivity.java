package uk.me.jamesharris.buses;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.ArrayList;


public class BusListActivity extends SingleFragmentActivity {

    public static final String EXTRA_STOPSPEC =
            "uk.me.jamesharris.buses.stop_spec";
    public static final String EXTRA_STOPCODE =
            "uk.me.jamesharris.buses.stop_code";

    @Override
    public Fragment createFragment() {
        //StopSpec stopSpec = getIntent().getParcelableExtra(EXTRA_STOPSPEC);
        int stopSpecPos = getIntent().getIntExtra(EXTRA_STOPSPEC,-1);
        //if (stopSpec != null) {
        if (stopSpecPos >= 0) {
            return BusListFragment.newInstance(stopSpecPos);
        }
        String stopCode = getIntent().getStringExtra(EXTRA_STOPCODE);
        if (!stopCode.isEmpty()) {
            return BusListFragment.newInstanceAlt(stopCode);
        }
        return new BusListFragment();
    }

    @Override
    public void onCreate(Bundle sis) {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(sis);
    }
}
