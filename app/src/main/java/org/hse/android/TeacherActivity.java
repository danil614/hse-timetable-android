package org.hse.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

public class TeacherActivity extends AppCompatActivity {
    private TextView time;
    private TextView status;
    private TextView subject;
    private TextView cabinet;
    private TextView corp;
    private TextView teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        final Spinner spinner = findViewById(R.id.groupList);

        List<StudentActivity.Group> groups = new ArrayList<>();
        initGroupList(groups);
        ArrayAdapter<?> adapter = new ArrayAdapter<>(context:
        this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              public void onItemSelected(AdapterView<?> parent,
                                          }
                View itemSelected, int selectedItemPosition, long selectedId){
            Object item = adapter.getItem(selectedItemPosition);
            Log.d(TAG, msg:"selectedItem: " + item);
            public void onNothingSelected (AdapterView < ? > parent){
            }
        }
    });
    time =

    findViewById(R.id.time);

    initTime();

    status=

    findViewById(R.id.status);

    subject =

    findViewById(R.id.subject);

    cabinet =

    findViewById(R.id.cabinet);

    corp =

    findViewById(R.id.corp);

    teacher =

    findViewById(R.id.teacher);

    initData();
}