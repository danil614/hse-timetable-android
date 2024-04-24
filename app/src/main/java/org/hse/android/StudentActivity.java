package org.hse.android;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.hse.android.database.entities.GroupEntity;
import org.hse.android.database.entities.TimeTableEntity;
import org.hse.android.database.entities.TimeTableWithTeacherEntity;
import org.hse.android.schedulefiles.Group;
import org.hse.android.schedulefiles.ScheduleMode;
import org.hse.android.schedulefiles.ScheduleType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentActivity extends BaseActivity {
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
        setContentView(R.layout.activity_student);

        // Инициализация элементов
        time = findViewById(R.id.time);
        status = findViewById(R.id.status);
        subject = findViewById(R.id.subject);
        cabinet = findViewById(R.id.cabinet);
        corp = findViewById(R.id.corp);
        teacher = findViewById(R.id.teacher);

        // Инициализация кнопок
        View scheduleDay = findViewById(R.id.buttonTimetableDay);
        scheduleDay.setOnClickListener(v -> showSchedule(ScheduleType.DAY));
        View scheduleWeek = findViewById(R.id.buttonTimetableWeek);
        scheduleWeek.setOnClickListener(v -> showSchedule(ScheduleType.WEEK));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        initGroupList();

        spinner = findViewById(R.id.groupList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                showTime(timeViewModel.getDateTime());
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void showSchedule(ScheduleType scheduleType) {
        Group selectedGroup = getSelectedGroup();
        if (selectedGroup == null) {
            return;
        }
        showScheduleImpl(ScheduleMode.STUDENT, scheduleType, selectedGroup);
    }

    protected void initGroupList() {
        mainViewModel.getGroups().observe(this, groupEntities -> {
            List<Group> groupsResult = new ArrayList<>();
            for (GroupEntity listEntity : groupEntities) {
                groupsResult.add(new Group(listEntity.id, listEntity.name));
            }
            adapter.clear();
            adapter.addAll(groupsResult);
        });
        timeViewModel.getDateTimeMutableLiveData().observe(this, this::showTime);
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

        TimeTableEntity timeTableEntity = timeTableWithTeacherEntity.timeTableEntity;
        status.setText(R.string.lesson_in_progress);
        subject.setText(timeTableEntity.subjName);
        cabinet.setText(timeTableEntity.cabinet);
        corp.setText(timeTableEntity.corp);
        teacher.setText(timeTableWithTeacherEntity.teacherEntity.fio);
    }

    @Override
    protected void showTime(Date dateTime) {
        super.showTime(dateTime);
        Group selectedGroup = getSelectedGroup();

        // по id группы и текущему времени надо обратиться к getTimeTableByDateAndGroupId и после этого вывести в initDataFromTimeTable
        if (selectedGroup != null) {
            mainViewModel.getTimeTableByDateAndGroupId(dateTime, selectedGroup.getId())
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