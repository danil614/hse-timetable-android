package org.hse.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeacherActivity extends BaseActivity {
    private static final String LOG_TAG = "LOG_TAG";
    private TextView status;
    private TextView subject;
    private TextView cabinet;
    private TextView corp;
    private TextView teacher;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        spinner = findViewById(R.id.groupList);

        List<StudentActivity.Group> groups = new ArrayList<>();
        initGroupList(groups);

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                Object item = adapter.getItem(selectedItemPosition);
                Log.d(LOG_TAG, "selectedItem: " + item);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        time = findViewById(R.id.time);
        initTime();

        status = findViewById(R.id.status);
        subject = findViewById(R.id.subject);
        cabinet = findViewById(R.id.cabinet);
        corp = findViewById(R.id.corp);
        teacher = findViewById(R.id.teacher);

        initData();

        View scheduleDay = findViewById(R.id.buttonTimetableDay);
        scheduleDay.setOnClickListener(v -> showSchedule(ScheduleType.DAY));
        View scheduleWeek = findViewById(R.id.buttonTimetableWeek);
        scheduleWeek.setOnClickListener(v -> showSchedule(ScheduleType.WEEK));
    }

    private void showSchedule(ScheduleType scheduleType) {
        Object selectedItem = spinner.getSelectedItem();
        if (!(selectedItem instanceof StudentActivity.Group)) {
            return;
        }
        StudentActivity.showScheduleImpl(this, ScheduleMode.TEACHER, scheduleType, (StudentActivity.Group) selectedItem, currentTime);
    }

    protected static void initGroupList(List<StudentActivity.Group> groups) {
        groups.add(new StudentActivity.Group(1, "Преподаватель 1"));
        groups.add(new StudentActivity.Group(2, "Преподаватель 2"));
    }

    private void initData() {
        status.setText(getString(R.string.status));
        subject.setText(getString(R.string.subject));
        cabinet.setText(getString(R.string.cabinet));
        corp.setText(getString(R.string.corp));
        teacher.setText(getString(R.string.teacher));
    }
}