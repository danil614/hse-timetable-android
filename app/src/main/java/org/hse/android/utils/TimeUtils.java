package org.hse.android.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TimeUtils {
    private static final String LOG_TAG = "LOG_TAG";
    private static final String URL = "https://www.timeapi.io/api/time/current/zone?timeZone=Asia/Yekaterinburg";
    private final OkHttpClient client = new OkHttpClient();
    private Date dateTime = new Date();

    public Date getTime() {
        Request request = new Request.Builder().url(URL).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                parseResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(LOG_TAG, "getTime", e);
            }
        });

        return dateTime;
    }

    private void parseResponse(Response response) {
        Gson gson = new Gson();
        ResponseBody body = response.body();

        try {
            if (body == null) {
                return;
            }
            String json = body.string();
            Log.d(LOG_TAG, json);
            TimeResponse timeResponse = gson.fromJson(json, TimeResponse.class);

            String currentTimeVal = timeResponse.getDateTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault());
            dateTime = simpleDateFormat.parse(currentTimeVal);
            Log.i(LOG_TAG, "parseResponse: " + dateTime);
        } catch (IOException | ParseException e) {
            Log.e(LOG_TAG, "parseResponse", e);
        }
    }
}
