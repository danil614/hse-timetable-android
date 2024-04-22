package org.hse.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import org.hse.android.schedulefiles.Group;
import org.hse.android.schedulefiles.ScheduleMode;
import org.hse.android.schedulefiles.ScheduleType;
import org.hse.android.utils.TimeResponse;
import org.hse.android.viewmodels.MainViewModel;
import org.hse.android.viewmodels.TimeViewModel;

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
    protected MainViewModel mainViewModel;
    protected TimeViewModel timeViewModel;

    private static final String LOG_TAG = "LOG_TAG";
    // public static final String URL = "https://api.ipgeolocation.io/ipgeo?apiKey=b03018f75ed94023a005637878ec0977";
    public static final String URL = "https://www.timeapi.io/api/time/current/zone?timeZone=Asia/Yekaterinburg";
    protected TextView time;

    private final OkHttpClient client = new OkHttpClient();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        timeViewModel = new ViewModelProvider(this).get(TimeViewModel.class);
        getTime();
    }

    protected void showScheduleImpl(ScheduleMode mode, ScheduleType type, Group group) {
        Intent intent = new Intent(this, ScheduleActivity.class);

        intent.putExtra(ScheduleActivity.ARG_NAME, group.getName());
        intent.putExtra(ScheduleActivity.ARG_ID, group.getId());
        intent.putExtra(ScheduleActivity.ARG_MODE, mode);
        intent.putExtra(ScheduleActivity.ARG_TYPE, type);
        intent.putExtra(ScheduleActivity.ARG_DATE, timeViewModel.dateMutableLiveData.getValue());
        startActivity(intent);
    }

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

    protected void showTime(Date dateTime) {
        if (dateTime == null) {
            return;
        }

        String formattedTimeDate = getFormattedTimeDate(dateTime);
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
            //String currentTimeVal = timeResponse.getTimeZone().getCurrentTime();
            //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
            //Date dateTime = simpleDateFormat.parse(currentTimeVal);
            String currentTimeVal = timeResponse.getDateTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault());
            Date dateTime = simpleDateFormat.parse(currentTimeVal);
            Log.i(LOG_TAG, "parseResponse" + dateTime);

            runOnUiThread(() -> timeViewModel.dateMutableLiveData.postValue(dateTime));
        } catch (IOException | ParseException e) {
            Log.e(LOG_TAG, "parseResponse", e);
        }
    }
}
