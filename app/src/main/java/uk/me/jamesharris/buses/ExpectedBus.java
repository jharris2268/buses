package uk.me.jamesharris.buses;

import android.text.format.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by james on 01/05/15.
 */
public class ExpectedBus {
    String mStopId, mLineName, mDirectionID,mDestinationName,mRegistrationNumber;
    Date mEstimatedTime;

    ExpectedBus(String stopId, String lineName, String directionID,
                String destinationName, String registrationNumber, long estimatedTime) {
        mStopId=stopId;
        mLineName=lineName;
        mDirectionID=directionID;
        mDestinationName=destinationName;
        mRegistrationNumber=registrationNumber;
        mEstimatedTime= new Date(estimatedTime);
    }

    public String getStopId() {
        return mStopId;
    }
    public String getLineName() {
        return mLineName;
    }

    public String getDirectionID() {
        return mDirectionID;
    }

    public String getDestinationName() {
        return mDestinationName;
    }

    public String getRegistrationNumber() {
        return mRegistrationNumber;
    }

    public Date getEstimatedTimeRaw() {
        return mEstimatedTime;
    }
    public String getEstimatedTime() {
        return (String) DateFormat.format("HH:mm:ss",mEstimatedTime);
    }
    public String getTimeTo() {
        return timeTo(mEstimatedTime);


    }



    public String toString() {
        return new StringBuilder().append(mLineName).append(" [").append(mDirectionID)
                .append("] to ").append(mDestinationName).append(" @ ")
                .append(mEstimatedTime.toString()).toString();
    }

    public static String timeTo(Date to) {
        Date now = new Date();
        long diffInMs = to.getTime() - now.getTime();
        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
        if (diffInSec < - 30.0) { return "past"; }
        if (diffInSec < 60.0) { return "now"; }
        long mins = diffInSec / 60;
        //long secs = diffInSec % 60;
        return String.format("in %2d min", mins);
    }
}
