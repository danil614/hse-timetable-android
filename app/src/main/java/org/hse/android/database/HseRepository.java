package org.hse.android.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.hse.android.database.entities.GroupEntity;
import org.hse.android.database.entities.TeacherEntity;
import org.hse.android.database.entities.TimeTableWithTeacherEntity;
import org.hse.android.utils.TimeResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HseRepository {
    private final HseDao dao;
    private final MutableLiveData<Date> dateMutableLiveData;
    private static final String LOG_TAG = "LOG_TAG";
    private static final String URL = "https://www.timeapi.io/api/time/current/zone?timeZone=Asia/Yekaterinburg";
    private final OkHttpClient client = new OkHttpClient();

    public HseRepository(Context context) {
        DatabaseManager databaseManager = DatabaseManager.getInstance(context);
        dao = databaseManager.getHseDao();
        dateMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<List<GroupEntity>> getGroups() {
        return dao.getAllGroups();
    }

    public LiveData<List<TeacherEntity>> getTeachers() {
        return dao.getAllTeachers();
    }

    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableTeacherByDate(Date date) {
        return dao.getTimeTableTeacher();
    }


    // По дате и по ID
    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableByDateAndGroupId(Date date, int id) {
        return dao.getTimeTableByDateAndGroupId(date, id);
    }

    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableByDateAndTeacherId(Date date, int id) {
        return dao.getTimeTableByDateAndTeacherId(date, id);
    }


    //  По временному промежутку и по ID
    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableStudentInRange(Date startDate, Date endDate, int id) {
        return dao.getTimeTableStudentInRange(startDate, endDate, id);
    }

    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableTeacherInRange(Date startDate, Date endDate, int id) {
        return dao.getTimeTableTeacherInRange(startDate, endDate, id);
    }

    private void getTime() {
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
            Date dateTime = simpleDateFormat.parse(currentTimeVal);
            dateMutableLiveData.postValue(dateTime);
            Log.i(LOG_TAG, "parseResponse: " + dateTime);
        } catch (IOException | ParseException e) {
            Log.e(LOG_TAG, "parseResponse", e);
        }
    }

    public void setDateTime() {
        getTime();
    }

    public MutableLiveData<Date> getDateTime() {
        return dateMutableLiveData;
    }
}
