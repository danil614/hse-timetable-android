package org.hse.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ScheduleActivity extends AppCompatActivity {
    private static final String LOG_TAG = "LOG_TAG";
    public static final String ARG_ID = "ARG_ID";
    public static final String ARG_TYPE = "ARG_TYPE";
    public static final String ARG_MODE = "ARG_MODE";
    public static final String ARG_DATE = "ARG_DATE";
    public static final Integer DEFAULT_ID = -1;

    private ScheduleType type;
    private ScheduleMode mode;
    private Integer id;
    private TextView title;
    private Date date;

    RecyclerView recyclerView;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        type = (ScheduleType) getIntent().getSerializableExtra(ARG_TYPE);
        mode = (ScheduleMode) getIntent().getSerializableExtra(ARG_MODE);
        id = getIntent().getIntExtra(ARG_ID, DEFAULT_ID);
        date = (Date) getIntent().getSerializableExtra(ARG_DATE);
        Log.i(LOG_TAG, String.valueOf(date));

        title = findViewById(R.id.scheduleTitle);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ItemAdapter(this::onScheduleItemClick);
        recyclerView.setAdapter(adapter);

        setTitle();
        initData();
    }

    private void setTitle() {
        String groupName = "";
        if (mode == ScheduleMode.STUDENT) {
            List<StudentActivity.Group> groups = new ArrayList<>();
            StudentActivity.initGroupList(groups);
            groupName = getGroupNameById(groups);
        } else if (mode == ScheduleMode.TEACHER) {
            List<StudentActivity.Group> groups = new ArrayList<>();
            TeacherActivity.initGroupList(groups);
            groupName = getGroupNameById(groups);
        }

        title.setText(groupName);
    }

    private String getGroupNameById(List<StudentActivity.Group> groups) {
        for (StudentActivity.Group group : groups) {
            if (Objects.equals(group.getId(), id)) {
                return group.getName();
            }
        }
        return "";
    }

    private void onScheduleItemClick(ScheduleItem scheduleItem) {
    }

    private static String getFormattedDate(Date currentTime) {
        if (currentTime == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM", Locale.forLanguageTag("ru"));
        return simpleDateFormat.format(currentTime);
    }

    private void initData() {
        List<ScheduleItem> list = new ArrayList<>();

        ScheduleItemHeader header;
        ScheduleItem item;

        switch (type) {
            case DAY:
                header = new ScheduleItemHeader();
                header.setTitle(getFormattedDate(date));
                list.add(header);

                item = new ScheduleItem();
                item.setStart("10:00");
                item.setEnd("11:00");
                item.setType("Практическое занятие");
                item.setName("Анализ данных (анг)");
                item.setPlace("Ауд. 503, Кочновский пр-д, д.3");
                item.setTeacher("Пред. Гущим Михаил Иванович");
                list.add(item);

                item = new ScheduleItem();
                item.setStart("12:00");
                item.setEnd("13:00");
                item.setType("Практическое занятие");
                item.setName("Анализ данныx (aнг)");
                item.setPlace("Ауд. 503, Кочновский пр-д, д.3");
                item.setTeacher("Пред. Гущим Михаил Иванович");
                list.add(item);

                break;
            case WEEK:
                // Понедельник
                header = new ScheduleItemHeader();
                header.setTitle("понедельник, 26 февраля");
                list.add(header);

                item = new ScheduleItem();
                item.setStart("09:00");
                item.setEnd("10:30");
                item.setType("Лекция");
                item.setName("Информатика");
                item.setPlace("Ауд. 301, ул. Ломоносова, д.5");
                item.setTeacher("Проф. Иванова Елена Петровна");
                list.add(item);

                item = new ScheduleItem();
                item.setStart("11:00");
                item.setEnd("12:30");
                item.setType("Лабораторная работа");
                item.setName("Программирование");
                item.setPlace("Ауд. 401, ул. Ломоносова, д.5");
                item.setTeacher("Доц. Петров Алексей Игоревич");
                list.add(item);

                item = new ScheduleItem();
                item.setStart("14:00");
                item.setEnd("15:30");
                item.setType("Семинар");
                item.setName("Математика");
                item.setPlace("Ауд. 201, ул. Ломоносова, д.5");
                item.setTeacher("Ст.преп. Сидорова Ольга Николаевна");
                list.add(item);

                // Вторник
                header = new ScheduleItemHeader();
                header.setTitle("вторник, 27 февраля");
                list.add(header);

                item = new ScheduleItem();
                item.setStart("09:00");
                item.setEnd("10:30");
                item.setType("Лекция");
                item.setName("Физика");
                item.setPlace("Ауд. 101, ул. Ломоносова, д.5");
                item.setTeacher("Проф. Смирнов Андрей Владимирович");
                list.add(item);

                item = new ScheduleItem();
                item.setStart("11:00");
                item.setEnd("12:30");
                item.setType("Лабораторная работа");
                item.setName("Химия");
                item.setPlace("Ауд. 201, ул. Ломоносова, д.5");
                item.setTeacher("Доц. Козлова Мария Ивановна");
                list.add(item);

                // Среда
                header = new ScheduleItemHeader();
                header.setTitle("среда, 28 февраля");
                list.add(header);

                item = new ScheduleItem();
                item.setStart("14:00");
                item.setEnd("15:30");
                item.setType("Семинар");
                item.setName("Биология");
                item.setPlace("Ауд. 301, ул. Ломоносова, д.5");
                item.setTeacher("Ст.преп. Никитина Елена Сергеевна");
                list.add(item);

                break;
        }

        adapter.setDataList(list);
    }
}