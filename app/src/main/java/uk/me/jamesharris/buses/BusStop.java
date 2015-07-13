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
        //mExpectedBuses=new ArrayList<>();
    }

    //public void addExpectedBus(ExpectedBus expectedBus) {
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

    //public ArrayList<ExpectedBus> getExpectedBuses() {
    public ExpectedBus getExpectedBus() {
        return mExpectedBus;
    }
/*
    public void sortExpectedBuses() {
        Collections.sort(mExpectedBuses, new Comparator<ExpectedBus>() {
            @Override
            public int compare(ExpectedBus lhs, ExpectedBus rhs) {
                return lhs.getEstimatedTime().compareTo(rhs.getEstimatedTime());
            }
        });
    }
*/
    public String toString() {
        StringBuilder sb= new StringBuilder().append("Stop ").append(mStopId)
                .append(" [").append(mStopPointName).append(" @")
                .append(mLatitude).append(", ").append(mLongitude)
                .append("]: ");
        /*sb.append("\n").append(mExpectedBuses.size()).append(" buses:");
        for (int i=0; i < mExpectedBuses.size(); i++) {
            sb.append("\n").append(i).append(mExpectedBuses.get(i).toString());
        }*/
        sb.append(mExpectedBus);
        return sb.toString();

    }

    public String titleString() {
        StringBuilder sb =  new StringBuilder();
        sb.append(mStopId).append(" ");
        sb.append(mStopPointName);
        if (!(mStopPointIndicator.equals("null")) ) {
            sb.append(" [").append(mStopPointIndicator).append("]");
        }
        if (!(mTowards.equals("null")) ) {
            sb.append(" to ").append(mTowards);
        }
        return sb.toString();
    }

    public String locationString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mLatitude).append(", ").append(mLongitude).append(": ");
        /*switch (mExpectedBuses.size()) {
            case 0:
                sb.append("no buses due");
                break;
            case 1:
                sb.append("1 bus");
                break;
            default:
                sb.append(mExpectedBuses.size()).append(" buses");
                break;
        }*/
        return sb.toString();
    }


}
