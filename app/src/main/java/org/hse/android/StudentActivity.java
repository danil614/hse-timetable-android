package org.hse.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentActivity extends AppCompatActivity {
    private static final String LOG_TAG = "LOG_TAG";
    private TextView time;
    private TextView status;
    private TextView subject;
    private TextView cabinet;
    private TextView corp;
    private TextView teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        final Spinner spinner = findViewById(R.id.groupList);

        List<Group> groups = new ArrayList<>();
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
    }


    static class Group {
        private Integer id;
        private String name;

        public Group(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private void initGroupList(List<Group> groups) {
        String[] educationalPrograms = {"ПИ", "БИ", "Э", "Ю", "ИЯ"};
        int[] years = {20, 21, 22, 23};
        int[] groupNumbers = {1, 2, 3};

        for (String program : educationalPrograms) {
            for (int year : years) {
                for (int number : groupNumbers) {
                    String groupName = program + "-" + year + "-" + number;
                    groups.add(new Group(groups.size() + 1, groupName));
                }
            }
        }
    }

    private void initTime() {
        time.setText(getFormattedTimeDate());
    }

    public static String getFormattedTimeDate() {
        Date currentTime = new Date();
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm", new Locale("ru", "RU"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", new Locale("ru", "RU"));

        String formattedTime = simpleTimeFormat.format(currentTime);
        String formattedDate = simpleDateFormat.format(currentTime);
        formattedDate = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1).toLowerCase();

        return formattedTime + ", " + formattedDate;
    }

    private void initData() {
        status.setText(getString(R.string.status));
        subject.setText(getString(R.string.subject));
        cabinet.setText(getString(R.string.cabinet));
        corp.setText(getString(R.string.corp));
        teacher.setText(getString(R.string.teacher));
    }
}