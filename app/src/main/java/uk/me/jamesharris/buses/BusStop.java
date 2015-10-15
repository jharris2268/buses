package uk.me.jamesharris.buses;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by james on 01/05/15.
 */


public class BusStop {
    String mStopPointName;
    String mStopId;
    String mTowards;
    String mStopPointIndicator;
    double mBearing,mLatitude,mLongitude;
    //ArrayList<ExpectedBus> mExpectedBuses;
    ExpectedBus mExpectedBus;

    BusStop(String stopPointName,String spotId, String towards,
            double bearing, String stopPointIndicator, double latitude, double longitude) {
        mStopPointName = stopPointName;
        mStopId = spotId;
        mTowards = towards;

        mBearing = bearing;
        mStopPointIndicator=stopPointIndicator;
        mLatitude = latitude;
        mLongitude = longitude;

    }


    public void setExpectedBus(ExpectedBus expectedBus) {
        mExpectedBus = expectedBus;
    }

    public String getStopPointName() {
        return mStopPointName;
    }

    public String getStopId() {
        return mStopId;
    }
    public String getStopPointIndicator() {
        return mStopPointIndicator;
    }
    public String getTowards() {
        return mTowards;
    }

    public double getBearing() {
        return mBearing;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }


    public ExpectedBus getExpectedBus() {
        return mExpectedBus;
    }
    public String toString() {
        StringBuilder sb= new StringBuilder().append("Stop ").append(mStopId)
                .append(" [").append(mStopPointName).append(" @")
                .append(mLatitude).append(", ").append(mLongitude)
                .append("]: ");

        sb.append(mExpectedBus);
        return sb.toString();

    }



}
