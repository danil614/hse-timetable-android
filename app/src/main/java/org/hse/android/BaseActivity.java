package org.hse.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import org.hse.android.schedulefiles.Group;
import org.hse.android.schedulefiles.ScheduleMode;
import org.hse.android.schedulefiles.ScheduleType;
import org.hse.android.viewmodels.MainViewModel;
import org.hse.android.viewmodels.TimeViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {
    protected MainViewModel mainViewModel;
    protected TimeViewModel timeViewModel;
    protected TextView time;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        timeViewModel = new ViewModelProvider(this).get(TimeViewModel.class);
    }

    protected void showScheduleImpl(ScheduleMode mode, ScheduleType type, Group group) {
        Intent intent = new Intent(this, ScheduleActivity.class);

        intent.putExtra(ScheduleActivity.ARG_NAME, group.getName());
        intent.putExtra(ScheduleActivity.ARG_ID, group.getId());
        intent.putExtra(ScheduleActivity.ARG_MODE, mode);
        intent.putExtra(ScheduleActivity.ARG_TYPE, type);
        intent.putExtra(ScheduleActivity.ARG_DATE, timeViewModel.getDateTime());
        startActivity(intent);
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
}
