package org.hse.android;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScheduleActivity extends BaseActivity {

    public static final String ARG_ID = "ARG_ID";
    public static final String ARG_TYPE = "ARG_TYPE";
    public static final String ARG_MODE = "ARG_MODE";
    public static final Integer DEFAULT_ID = -1;

    private ScheduleType type;
    private ScheduleMode mode;
    private Integer id;
    private TextView title;

    RecyclerView recyclerView;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        type = (ScheduleType) getIntent().getSerializableExtra(ARG_TYPE);
        mode = (ScheduleMode) getIntent().getSerializableExtra(ARG_MODE);
        id = getIntent().getIntExtra(ARG_ID, DEFAULT_ID);

        title = findViewById(R.id.scheduleTitle);
        time = findViewById(R.id.scheduleTime);

        initTime();
        setTitle();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ItemAdapter(this::onScheduleItemClick);
        recyclerView.setAdapter(adapter);

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

    private void initData() {
        List<ScheduleItem> list = new ArrayList<>();

        ScheduleItemHeader header = new ScheduleItemHeader();
        header.setTitle(getFormattedDate());
        list.add(header);

        ScheduleItem item = new ScheduleItem();
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
        adapter.setDataList(list);
    }
}