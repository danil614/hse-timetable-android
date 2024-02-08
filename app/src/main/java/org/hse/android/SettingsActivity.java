package org.hse.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity implements SensorEventListener {
    private static final String LOG_TAG = "LOG_TAG";
    private static final String PERMISSION = "android.permission.CAMERA";
    private static final int REQUEST_PERMISSION_CODE = 100;
    String mCurrentPhotoPath;
    private SensorManager sensorManager;
    private Sensor light;
    private TextView sensorLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorLight = findViewById(R.id.sensorLight);

        View buttonTakePhoto = findViewById(R.id.buttonTakePhoto);
        buttonTakePhoto.setOnClickListener(v -> checkPermission());
    }

    public void checkPermission() {
        int permissionCheck = ActivityCompat.checkSelfPermission(this, PERMISSION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)) {
                showExplanation("Нужно предоставить права",
                        "Для снятия фото нужно предоставить права на фото",
                        PERMISSION, REQUEST_PERMISSION_CODE);
            } else {
                requestPermissions(new String[]{PERMISSION}, REQUEST_PERMISSION_CODE);
            }
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(LOG_TAG, "Create file", ex);
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

                try {
                    startActivityForResult(takePictureIntent, REQUEST_PERMISSION_CODE);
                } catch (ActivityNotFoundException ex) {
                    Log.e(LOG_TAG, "Start activity", ex);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Создание файла с уникальным именем
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* префикс */
                ".jpg",         /* расширение */
                storageDir      /* директория */
        );

        // Сохраняем путь для использования с интентом ACTION_VIEW
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void showExplanation(String title, String message, String permission, int requestPermissionCode) {
        showToast(message);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        sensorLight.setText(lux + " lux");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}