package org.hse.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "LOG_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View buttonStudent = findViewById(R.id.buttonStudent);
        View buttonTeacher = findViewById(R.id.buttonTeacher);
        View buttonSettings = findViewById(R.id.buttonSettings);

        buttonStudent.setOnClickListener(v -> showStudent());
        buttonTeacher.setOnClickListener(v -> showTeacher());
        buttonSettings.setOnClickListener(v -> showSettings());

        Log.i(LOG_TAG, "On Create");
    }

    private void showStudent() {
        Intent intent = new Intent(this, StudentActivity.class);
        startActivity(intent);
    }

    private void showTeacher() {
        Intent intent = new Intent(this, TeacherActivity.class);
        startActivity(intent);
    }

    private void showSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}