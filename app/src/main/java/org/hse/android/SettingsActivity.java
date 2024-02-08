package org.hse.android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements SensorEventListener {
    private static final String LOG_TAG = "LOG_TAG";
    private static final String PERMISSION = "android.permission.CAMERA";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;
    private SensorManager sensorManager;
    private Sensor light;
    private TextView sensorLight;
    private TextView name;
    private TextView sensorsTextView;
    private ImageView imageView;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferenceManager = new PreferenceManager(this);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorLight = findViewById(R.id.sensorLight);
        imageView = findViewById(R.id.imageViewUser);
        name = findViewById(R.id.name);
        sensorsTextView = findViewById(R.id.sensorsTextView);
        sensorsTextView.setMovementMethod(new ScrollingMovementMethod());

        displaySensorList();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Получаем сохраненное имя
        name.setText(preferenceManager.getValue("name", ""));

        View buttonTakePhoto = findViewById(R.id.buttonTakePhoto);
        buttonTakePhoto.setOnClickListener(v -> checkPermission());

        View buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(v -> saveSettings());

        // Получаем сохраненную картинку пользователя
        String path = preferenceManager.getValue("path", "");
        setUserView(path);
    }

    private void displaySensorList() {
        // Получаем список всех датчиков
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        StringBuilder sensorInfo = new StringBuilder();

        // Перебираем все датчики в списке и выводим информацию о них
        for (Sensor sensor : sensorList) {
            sensorInfo.append(sensor.getName()).append("\n");
        }

        // Выводим информацию о датчиках в TextView
        sensorsTextView.setText(sensorInfo.toString());
    }

    private void saveSettings() {
        // Сохраняем имя
        preferenceManager.saveValue("name", name.getText().toString());
        // Сохраняем картинку
        preferenceManager.saveValue("path", currentPhotoPath);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void checkPermission() {
        int permissionCheck = ActivityCompat.checkSelfPermission(this, PERMISSION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)) {
                showExplanation("Нужно предоставить права",
                        "Для снятия фото нужно предоставить права на фото",
                        PERMISSION, REQUEST_IMAGE_CAPTURE);
            } else {
                requestPermissions(new String[]{PERMISSION}, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createImageFile();
            Uri photoURI = null;

            try {
                photoURI = FileProvider.getUriForFile(
                        this,
                        getPackageName() + ".provider",
                        photoFile);
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Get Uri for file", ex);
            }

            Log.i(LOG_TAG, "Photo URI: " + photoURI);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                Log.i(LOG_TAG, "Picture successfully saved: " + takePictureIntent);
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Start activity", ex);
            }
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_USER_VIEW_" + timeStamp + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName);

        // Сохраняем путь
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(LOG_TAG, "onActivityResult entered: ");
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                setUserView(currentPhotoPath);
            }
        }
    }

    private void setUserView(String path) {
        if (path == null || path.isEmpty()) {
            return;
        }

        Bitmap imgBitmap = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(imgBitmap);
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> requestPermission(permission, permissionRequestCode));
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
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
