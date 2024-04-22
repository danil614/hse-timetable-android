package org.hse.android.utils;

import com.google.gson.annotations.SerializedName;

public class TimeResponse {
    @SerializedName("time_zone")
    private TimeZone timeZone;

    @SerializedName("dateTime")
    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
