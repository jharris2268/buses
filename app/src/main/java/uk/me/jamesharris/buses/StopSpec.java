package uk.me.jamesharris.buses;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import uk.me.jamesharris.buses.BusJsonLoader;

/**
 * Created by james on 18/06/15.
 */
class StopSpec {
    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    private long mId;

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    private String mDescription;

    private double mLon, mLat, mRadius;
    private ArrayList<String> mStopList;

    ArrayList<BusStop> loadBuses() throws IOException {
        if (mStopList!=null) {
            return BusJsonLoader.loadBusesForStops(mStopList.toArray(new String[]{}));
        } else if (mRadius != 0){
            return BusJsonLoader.loadBusesNear(mLat, mLon, mRadius);
        } else {
            return BusJsonLoader.loadBusesNear(51.39, 0.1, 200);
        }
    }

    public void addStop(String stopName) {
        if (mStopList == null) {
            mStopList = new ArrayList<>();
        }
        mStopList.add(stopName);
    }
    int numStops() {
        if (mStopList==null) { return 0; }
        return mStopList.size();
    }
    String getStop(int idx) {
        if (mStopList==null) { return ""; }
        if ((idx < 0) || (idx >= mStopList.size())) {
            return "";
        }
        return mStopList.get(idx);
    }

    public static StopSpec getDefault() {
        StopSpec ans=new StopSpec();
        ans.setId(0);
        ans.setDescription("Default");
        ans.mStopList = new ArrayList<String>(Arrays.asList(new String[]{"BP2292", "BP2287", "R0553", "26136", "R0514", "18997", "35572", "18952"}));
        return ans;
    }

    public static StopSpec fromJson(String json) throws JSONException {

        JSONObject obj = new JSONObject(json);

        StopSpec val=new StopSpec();
        val.setId(obj.getLong("id"));
        val.setDescription(obj.getString("description"));
        JSONArray stops = obj.getJSONArray("stops");
        for (int i=0; i < stops.length(); i++) {
            val.addStop(stops.getString(i));
        }
        return val;
    }

    public String toJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", mId);
        obj.put("description", mDescription);

        JSONArray stops = new JSONArray();
        for (String s : mStopList) {
            stops.put(s);
        }
        obj.put("stops",stops);

        return obj.toString();
    }

    public static StopSpec inCircle(double lat, double lon, double radius) {
        StopSpec stopSpec = new StopSpec();
        stopSpec.setLatLonRadius(lat,lon,radius);
        stopSpec.setDescription(String.format("@ %8.5f, %8.5f [%5.0fm]", lon, lat, radius));
        return stopSpec;
    }
    public void setLatLonRadius(double lat, double lon, double radius) {

        mLat=lat;
        mLon=lon;
        mRadius=radius;

    }
}