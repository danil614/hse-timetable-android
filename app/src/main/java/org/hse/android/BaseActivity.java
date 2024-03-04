package org.hse.android;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.hse.android.utils.TimeResponse;

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

public abstract class BaseActivity extends AppCompatActivity {
    private static final String LOG_TAG = "LOG_TAG";
    public static final String URL = "https://api.ipgeolocation.io/ipgeo?apiKey=b03018f75ed94023a005637878ec0977";

    protected TextView time;
    protected Date currentTime;

    private final OkHttpClient client = new OkHttpClient();

    protected void getTime() {
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
    }

    protected void initTime() {
        getTime();
    }

    private void showTime(Date dateTime) {
        if (dateTime == null) {
            return;
        }
        currentTime = dateTime;
        String formattedTimeDate = getFormattedTimeDate(currentTime);
        time.setText(formattedTimeDate);
    }

    protected static String getFormattedTimeDate(Date currentTime) {
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm", Locale.forLanguageTag("ru"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.forLanguageTag("ru"));

        String formattedTime = simpleTimeFormat.format(currentTime);
        String formattedDate = simpleDateFormat.format(currentTime);
        formattedDate = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1).toLowerCase();

        return formattedTime + ", " + formattedDate;
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
            String currentTimeVal = timeResponse.getTimeZone().getCurrentTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
            Date dateTime = simpleDateFormat.parse(currentTimeVal);

            runOnUiThread(() -> showTime(dateTime));
        } catch (IOException | ParseException e) {
            Log.e(LOG_TAG, "parseResponse", e);
        }
    }
}
