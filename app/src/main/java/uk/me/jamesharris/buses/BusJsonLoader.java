package uk.me.jamesharris.buses;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by james on 02/05/15.
 */
public class BusJsonLoader extends JsonLoader {
    private static final String TAG = "BusJsonLoader";
    private static final String Url = "http://countdown.api.tfl.gov.uk/interfaces/ura/instant_V1?";
    private static final String[] Columns = new String[]{
            "StopPointName","StopID","Towards","Bearing","StopPointIndicator","Latitude","Longitude",
            "LineName","DirectionID","DestinationName","RegistrationNumber","EstimatedTime"
    };
    public static ArrayList<BusStop> loadBuses(String url) throws IOException {
        ArrayList<String> lines = getUrlLines(url);
        return loadBusesFromIterable(lines);
    }

    public static ArrayList<BusStop> loadBusesFromIterable(Iterable<String> lines) throws IOException {
        //Map<String,BusStop> res = new HashMap<String,BusStop>();
        ArrayList<BusStop> res = new ArrayList<BusStop>();
        int i = 0;
        for (String line : lines) {
            try {
                JSONArray arr = new JSONArray(line);
                if (arr.getInt(0)==1) {
                    if (arr.length()!=Columns.length+1) {
                        throw new JSONException("too short");
                    }
                    BusStop busStop =
                    //if (!res.containsKey(arr.getString(2))) {
                        /*res.put(arr.getString(2),*/ new BusStop(arr.getString(1), arr.getString(2), arr.getString(3),
                                arr.getDouble(4),arr.getString(5),arr.getDouble(6), arr.getDouble(7));
                    //}
                    ExpectedBus bus = new ExpectedBus(arr.getString(2),arr.getString(8),arr.getString(9),arr.getString(10),arr.getString(11),arr.getLong(12));
                    busStop.setExpectedBus(bus);
                    //res.get(arr.getString(2)).addExpectedBus(bus);
                    res.add(busStop);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Can't parse line " + i + ": " + line, e);
            }
            i++;
        }
        Log.i(TAG,new StringBuilder().append("Found ").append(res.size()).append("stops").toString());
        //return new ArrayList<BusStop>(res.values());
        Collections.sort(res, new Comparator<BusStop>() {
            @Override
            public int compare(BusStop lhs, BusStop rhs) {
                return lhs.getExpectedBus().getEstimatedTime().compareTo(rhs.getExpectedBus().getEstimatedTime());
            }
        });
        return res;
    }

    public static ArrayList<BusStop> loadBusesForStops(String[] stops) throws IOException {
        StringBuilder query = new StringBuilder();
        query.append(Url);
        query.append("ReturnList=");
        query.append(TextUtils.join(",", Columns));
        query.append("&StopID=");
        query.append(TextUtils.join(",", stops));
        return loadBuses(query.toString());
    }

    public static ArrayList<BusStop> loadBusesNear(double lat, double lon, double radius) throws IOException {
        StringBuilder query = new StringBuilder();
        query.append(Url);
        query.append("ReturnList=");
        query.append(TextUtils.join(",", Columns));
        query.append("&Circle=");
        query.append(lat).append(",");
        query.append(lon).append(",");
        query.append(radius);
        return loadBuses(query.toString());
    }

    public static ArrayList<BusStop> loadParticularBus(String registrationNumber) throws IOException {
        StringBuilder query = new StringBuilder();
        query.append(Url);
        query.append("ReturnList=");
        query.append(TextUtils.join(",", Columns));
        query.append("&RegistrationNumber=");
        query.append(registrationNumber);
        return loadBuses(query.toString());
    }

}
