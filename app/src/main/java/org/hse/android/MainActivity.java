package org.hse.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "HSE_INFORMATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View buttonStudent = findViewById(R.id.buttonStudent);
        View buttonTeacher = findViewById(R.id.buttonTeacher);

        buttonStudent.setOnClickListener(v -> showStudent());
        buttonTeacher.setOnClickListener(v -> showTeacher());

        Log.i(LOG_TAG, "On Create");
    }

    private void showStudent() {
        //showToast("Страница для студентов");

        Intent intent = new Intent(this, StudentActivity.class);
        startActivity(intent);
    }

    private void showTeacher() {
        //showToast("Страница для преподавателей");

        Intent intent = new Intent(this, TeacherActivity.class);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}