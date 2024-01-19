package org.hse.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends AppCompatActivity {

    private TextView result;
    private EditText number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        number = findViewById(R.id.number);
        View button = findViewById(R.id.button);
        View button2 = findViewById(R.id.button2);

        result = findViewById(R.id.result);

        button.setOnClickListener(v -> clickButton());

        button2.setOnClickListener(v -> clickButton2());
    }

    private void clickButton() {
        Integer count = validateNumber(-10000, 10000);

        if (count == null) {
            return;
        }

        // init list
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(i + 1);
        }

        // Count all items in list
        int sum = list.stream().mapToInt(Integer::intValue).sum();
        result.setText(String.format("Result1: %d", sum));
    }

    private void clickButton2() {
        int count = validateNumber(-30, 30);

        // init list
        int mul = 1;

        for (int i = 1; i <= count; i++) {
            if (i % 2 == 0) {
                mul *= i;
            }
        }

        result.setText(String.format("Result2: %d", mul));
    }

    private Integer validateNumber(int min, int max) {
        String numberVal = number.getText().toString();
        if (numberVal.isEmpty()) {
            numberVal = "0";
        }
        int value = Integer.parseInt(numberVal);

        if (min > value || value > max) {
            return null;
        } else {
            return value;
        }
    }
}