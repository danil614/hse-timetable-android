package org.hse.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScheduleActivity extends AppCompatActivity {

    public static final String ARG_ID = "ARG_ID";
    public static final String ARG_TYPE = "ARG_TYPE";
    public static final String ARG_MODE = "ARG_MODE";
    public static final Integer DEFAULT_ID = -1;

    private ScheduleType type;
    private ScheduleMode mode;
    private Integer id;

    RecyclerView recyclerView;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        type = (ScheduleType) getIntent().getSerializableExtra(ARG_TYPE);
        mode = (ScheduleMode) getIntent().getSerializableExtra(ARG_MODE);
        id = getIntent().getIntExtra(ARG_ID, DEFAULT_ID);

        TextView title = findViewById(R.id.title);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ItemAdapter(this::onScheduleItemClick);
        recyclerView.setAdapter(adapter);
    }

    private void onScheduleItemClick(ScheduleItem scheduleItem) {
    }
}