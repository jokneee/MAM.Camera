package com.example.lab2;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;


public class MainActivity extends Activity implements SensorEventListener {

    RelativeLayout rl;
    SensorManager sm;
    MyView myCameraOverlay;
    Preview myCameraView;
    private Sensor magnetic;
    private Sensor acceleration;

    float[] magneticArr;
    float[] accelerationArr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 0);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }

        SpecificLocation pg = new SpecificLocation("Gmach Główny PG",18.61892152327219, 54.37196228373367);

        SpecificLocation sopot  = new SpecificLocation("Molo w Sopocie",  18.573485569809534 ,54.4471465618685);

        SpecificLocation myLocation = new SpecificLocation("Moja lokalizacja",18.499237893244327 , 54.35358311113001);

        rl = findViewById(R.id.cameraLayout);

        myCameraView = new Preview(this);
        rl.addView(myCameraView);

        myCameraOverlay = new MyView(this);
        rl.addView(myCameraOverlay);

        myCameraOverlay.setMyLocation(myLocation);
        myCameraOverlay.setLocations(new ArrayList<>(Arrays.asList(pg, sopot)));

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        magnetic = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        acceleration = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor def = sm.getDefaultSensor(Sensor.TYPE_ALL);
        sm.registerListener(this, def, SensorManager.SENSOR_DELAY_NORMAL);
        registerSensor(acceleration);
        registerSensor(magnetic);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerationArr = event.values.clone();
                refreshPreview();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticArr = event.values.clone();
                refreshPreview();
                break;
            default:
                myCameraView.invalidate();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 0);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
    }

    private void registerSensor(Sensor sensor) {
        if (sensor != null) {
            sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void refreshPreview() {
        if (accelerationArr != null && magneticArr != null) {
            float[] R = new float[9];
            SensorManager.getRotationMatrix(R, null, accelerationArr, magneticArr);
            setViewOrientation(R);
            setViewDeviceVector(R);
            myCameraOverlay.invalidateLocations();
            myCameraOverlay.invalidate();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setViewOrientation(float[] rotationMatrix) {
        float[] orientation = new float[3];
        SensorManager.getOrientation(rotationMatrix, orientation);
        double[] orientationInDegrees = IntStream.range(0, orientation.length)
                .mapToDouble(i -> orientation[i])
                .map(Math::toDegrees)
                .toArray();
        this.myCameraOverlay.setOrientation(orientationInDegrees);
    }

    private void setViewDeviceVector(float[] rotationMatrix) {
        float[] cameraVector = {0, 0, -1};
        float[] deviceVector = new float[3];
        deviceVector[0] = rotationMatrix[0] * cameraVector[0] +
                rotationMatrix[1] * cameraVector[1] +
                rotationMatrix[2] * cameraVector[2];
        deviceVector[1] = rotationMatrix[3] * cameraVector[0] +
                rotationMatrix[4] * cameraVector[1] +
                rotationMatrix[5] * cameraVector[2];
        deviceVector[2] = rotationMatrix[6] * cameraVector[0] +
                rotationMatrix[7] * cameraVector[1] +
                rotationMatrix[8] * cameraVector[2];
        this.myCameraOverlay.setDeviceVector(deviceVector);
    }
}