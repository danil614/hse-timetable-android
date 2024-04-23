package org.hse.android.utils;

import com.google.gson.annotations.SerializedName;

public class TimeResponse {
    @SerializedName("dateTime")
    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }
}
