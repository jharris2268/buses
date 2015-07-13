package uk.me.jamesharris.buses;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by james on 18/06/15.
 */
public class BusManager {
    private static final String TAG = "BusManager";
    public static final String ACTION_LOCATION =
            "uk.me.jamesharris.buses.ACTION_LOCATION";

    private static final String PREFS_FILE = "stopspecs";
    private static final String PREFS_STOPSPECS = "BusManager.latestStopSpec";

    private static BusManager sBusManager;
    private Context mAppContext;
    private LocationManager mLocationManager;
    private SharedPreferences mPrefs;

    private ArrayList<StopSpec> mStopSpecs;

    private BusManager(Context appContext) {
        mAppContext=appContext;
        mLocationManager=(LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
        mStopSpecs = new ArrayList<>();
        mPrefs = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        fetchSavedStopSpecs();

    }

    public static BusManager get(Context c) {
        if (sBusManager==null) {
            sBusManager = new BusManager(c.getApplicationContext());
        }
        return sBusManager;
    }


    public int numStopSpecs() {
        return mStopSpecs.size()+1;
    }
    public StopSpec getStopSpec(int i) {
        if (i==mStopSpecs.size()) {
            if (mLocationManager!=null) {
                Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (locationGPS != null) {
                    return StopSpec.inCircle(locationGPS.getLatitude(), locationGPS.getLongitude(), 400.0);
                }
            }
            return StopSpec.inCircle(51.495958, -0.143929, 100.0);
        }
        return mStopSpecs.get(i);
    }

    private void fetchSavedStopSpecs() {
        String stopSpecString = mPrefs.getString(PREFS_STOPSPECS,"");
        if (stopSpecString.isEmpty()) {
            Log.i(TAG, "no saved stopspecs");
            return;
        }

        TextUtils.StringSplitter splitter = new TextUtils.SimpleStringSplitter('\n');
        splitter.setString(stopSpecString);
        try {
            mStopSpecs = readStopSpecsJson(splitter);
        } catch (JSONException e) {
            Log.i(TAG, "Failed to load stopspecs "+e.toString());
        }
    }
    private void saveStopSpecs(ArrayList<String> spec) {
        SharedPreferences.Editor editor = mPrefs.edit();
        String stopSpecString = TextUtils.join("\n", spec);
        editor.putString(PREFS_STOPSPECS,stopSpecString);
        editor.commit();
        Log.i(TAG,"wrote "+stopSpecString.length()+" bytes to SavedPreferences");
    }

    public void refreshStopList() {
        try {

            ArrayList<String> newSpecJson = JsonLoader.getUrlLines("http://www.jamesharris.me.uk/stopspec.json");
            mStopSpecs = readStopSpecsJson(newSpecJson);
            saveStopSpecs(newSpecJson);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<StopSpec> readStopSpecsJson(Iterable<String> newSpecJson) throws JSONException {
        ArrayList<StopSpec> newSpec = new ArrayList<>();

        for (String line : newSpecJson) {
            try {
                JSONArray arr = new JSONArray(line);
                if (arr.length() == 0) {
                    throw new JSONException("empty line");
                }
                StopSpec spec = new StopSpec();
                spec.setDescription(arr.getString(0));
                if (arr.length() > 1) {
                    for (int i = 1; i < arr.length(); i++) {
                        spec.addStop(arr.getString(i));
                    }
                }
                newSpec.add(spec);
            } catch (JSONException e) {
                StopSpec spec = StopSpec.fromJson(line);
                newSpec.add(spec);
            }
        }
        return newSpec;
    }
}
