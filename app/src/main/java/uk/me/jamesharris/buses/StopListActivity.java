package uk.me.jamesharris.buses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by james on 18/06/15.
 */
public class StopListActivity extends SingleFragmentActivity {
    /*First screen: list of known stop specs*/
    public static final String EXTRA_STOP_LIST =
            "uk.me.jamesharris.runtracker.runtracker.stop_list_spec";



    @Override
    public Fragment createFragment() {
        return new StopListFragment();
    }

    @Override
    public void onCreate(Bundle sis) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(sis);
    }
}
