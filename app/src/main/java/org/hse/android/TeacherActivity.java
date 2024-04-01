package org.hse.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.hse.android.database.entities.TeacherEntity;
import org.hse.android.database.entities.TimeTableEntity;
import org.hse.android.database.entities.TimeTableWithTeacherEntity;
import org.hse.android.schedulefiles.Group;
import org.hse.android.schedulefiles.ScheduleMode;
import org.hse.android.schedulefiles.ScheduleType;

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

    private ArrayAdapter<Group> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        spinner = findViewById(R.id.groupList);

        initGroupList();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                Object item = adapter.getItem(selectedItemPosition);
                Log.d(LOG_TAG, "selectedItem: " + item);
                showTime(timeViewModel.dateMutableLiveData.getValue());
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        time = findViewById(R.id.time);
        status = findViewById(R.id.status);
        subject = findViewById(R.id.subject);
        cabinet = findViewById(R.id.cabinet);
        corp = findViewById(R.id.corp);
        teacher = findViewById(R.id.teacher);

        initTime();
        initData();

        View scheduleDay = findViewById(R.id.buttonTimetableDay);
        scheduleDay.setOnClickListener(v -> showSchedule(ScheduleType.DAY));
        View scheduleWeek = findViewById(R.id.buttonTimetableWeek);
        scheduleWeek.setOnClickListener(v -> showSchedule(ScheduleType.WEEK));
    }

    private void showSchedule(ScheduleType type) {
        Object selectedItem = spinner.getSelectedItem();
        if (!(selectedItem instanceof Group)) {
            return;
        }
        showScheduleImpl(ScheduleMode.TEACHER, type, (Group) selectedItem);
    }

    private void initGroupList() {
        mainViewModel.getTeachers().observe(this, list -> {
            List<Group> groups = new ArrayList<>();
            for (TeacherEntity listEntity : list) {
                groups.add(new Group(listEntity.id, listEntity.fio));
            }
            adapter.clear();
            adapter.addAll(groups);
        });
    }

    private void initTime() {
        timeViewModel.dateMutableLiveData.observe(this, this::showTime);
    }

    private void initData() {
        initDataFromTimeTable(null);
    }

    private void initDataFromTimeTable(TimeTableWithTeacherEntity timeTableWithTeacherEntity) {
        if (timeTableWithTeacherEntity == null) {
            status.setText(getString(R.string.status));
            subject.setText(getString(R.string.subject));
            cabinet.setText(getString(R.string.cabinet));
            corp.setText(getString(R.string.corp));
            teacher.setText(getString(R.string.teacher));
            return;
        }

        status.setText(R.string.lesson_in_progress);

        TimeTableEntity timeTableEntity = timeTableWithTeacherEntity.timeTableEntity;

        subject.setText(timeTableEntity.subjName);
        cabinet.setText(timeTableEntity.cabinet);
        corp.setText(timeTableEntity.corp);
        teacher.setText(timeTableWithTeacherEntity.teacherEntity.fio);
    }

    @Override
    protected void showTime(Date dateTime) {
        super.showTime(dateTime);
        Group teacher = getSelectedGroup();

        // по id учителя и текущему времени надо обратиться к getTimeTableByDateAndGroupId и после этого вывести в initDataFromTimeTable
        if (teacher != null) {
            mainViewModel.getTimeTableByDateAndTeacherId(dateTime, teacher.getId())
                    .observe(this, timeTableWithTeacherEntities -> {
                        if (timeTableWithTeacherEntities != null &&
                                !timeTableWithTeacherEntities.isEmpty()) {
                            initDataFromTimeTable(timeTableWithTeacherEntities.get(0));
                        } else
                            initDataFromTimeTable(null);
                    });

        }
    }

    private Group getSelectedGroup() {
        Object selectedItem = spinner.getSelectedItem();

        if (!(selectedItem instanceof Group)) {
            return null;
        }

        return (Group) selectedItem;
    }
}