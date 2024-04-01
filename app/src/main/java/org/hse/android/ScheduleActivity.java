package org.hse.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.android.database.entities.TimeTableEntity;
import org.hse.android.database.entities.TimeTableWithTeacherEntity;
import org.hse.android.schedulefiles.ItemAdapter;
import org.hse.android.schedulefiles.ScheduleItem;
import org.hse.android.schedulefiles.ScheduleItemHeader;
import org.hse.android.schedulefiles.ScheduleMode;
import org.hse.android.schedulefiles.ScheduleType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends BaseActivity {
    private static final String LOG_TAG = "LOG_TAG";
    public static final String ARG_ID = "ARG_ID";
    public static final String ARG_TYPE = "ARG_TYPE";
    public static final String ARG_MODE = "ARG_MODE";
    public static String ARG_NAME = "ARG_NAME";
    public static final String ARG_DATE = "ARG_DATE";
    public static final Integer DEFAULT_ID = -1;

    private ScheduleType type;
    private ScheduleMode mode;
    private Integer id;
    private TextView title;
    private Date date;
    private String name;

    private ItemAdapter adapter;
    List<ScheduleItem> listOfScheduleItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        type = (ScheduleType) getIntent().getSerializableExtra(ARG_TYPE);
        mode = (ScheduleMode) getIntent().getSerializableExtra(ARG_MODE);
        name = getIntent().getStringExtra(ARG_NAME);
        id = getIntent().getIntExtra(ARG_ID, DEFAULT_ID);
        date = (Date) getIntent().getSerializableExtra(ARG_DATE);
        title = findViewById(R.id.scheduleTitle);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ItemAdapter(this::onScheduleItemClick);
        recyclerView.setAdapter(adapter);

        initTitle();
        initData();
    }

    private void onScheduleItemClick(ScheduleItem scheduleItem) {
    }

    private void initTitle() {
        title.setText(name);
    }

    private void initData() {
        Observer<List<TimeTableWithTeacherEntity>> observer = timeTableWithTeacherEntities -> {
            // Создать расписание из timeTableWithTeacherEntities
            listOfScheduleItems = scheduleBuilder(timeTableWithTeacherEntities);
            // Прикрепить его к адаптеру
            adapter.setDataList(listOfScheduleItems);

        };

        switch (type) {
            case DAY: {
                mainViewModel.getTimeTableForDay(date, id, mode).observe(this, observer);
                break;
            }
            case WEEK: {
                mainViewModel.getTimeTableForWeek(date, id, mode).observe(this, observer);
                break;
            }
        }
    }

    private List<ScheduleItem> scheduleBuilder(List<TimeTableWithTeacherEntity> entities) {
        List<ScheduleItem> list = new ArrayList<>();

        if (entities == null || entities.isEmpty()) {
            Toast.makeText(this, R.string.no_lessons, Toast.LENGTH_SHORT).show();
            return list;
        }

        // Для отображения даты
        SimpleDateFormat headerDateFormat = new SimpleDateFormat("EEEE, dd MMMM", new Locale("ru"));
        SimpleDateFormat hoursFormat = new SimpleDateFormat("HH:mm", new Locale("ru"));

        String lastDate = "";
        String currentDate;

        for (TimeTableWithTeacherEntity entity : entities) {
            TimeTableEntity timeTableEntity = entity.timeTableEntity;

            // Проверяем, есть ли такой заголовок, если нет, то создаём новый
            currentDate = headerDateFormat.format(timeTableEntity.timeStart);
            if (!currentDate.equals(lastDate)) {
                ScheduleItemHeader header = new ScheduleItemHeader();
                header.setTitle(headerDateFormat.format(timeTableEntity.timeEnd));
                list.add(header);

                lastDate = currentDate;
            }

            String start = hoursFormat.format(timeTableEntity.timeStart);
            String end = hoursFormat.format(timeTableEntity.timeEnd);
            String type = getLessonType(timeTableEntity.type);
            String name = timeTableEntity.subjName;
            String place = timeTableEntity.cabinet + ", " + timeTableEntity.corp;
            String teacher_fio = entity.teacherEntity.fio;

            ScheduleItem item = new ScheduleItem();
            item.setStart(start);
            item.setEnd(end);
            item.setType(type);
            item.setName(name);
            item.setPlace(place);
            item.setTeacher(teacher_fio);
            list.add(item);
        }
        return list;
    }

    public static String getLessonType(int number) {
        switch (number) {
            case 0:
                return "Лекция";
            case 1:
                return "Семинар";
            case 2:
                return "Практическое занятие";
            default:
                return "–";
        }
    }
}