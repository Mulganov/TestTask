package com.mulganov.testtask.view.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mulganov.testtask.R;

import java.util.ArrayList;

public class Fragment_2 extends Fragment implements SensorEventListener {

    private static ArrayList<String> list = new ArrayList<>();

    public static Fragment_2 newInstance() {
        return new Fragment_2();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_2, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
System.out.println("sensor");
        float x = event.values[2];

        ImageView image_1 = getActivity().findViewById(R.id.image_1);

        ImageView image_2 = getActivity().findViewById(R.id.image_2);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image_1.setX(image_1.getX() + x * 10 / 2.5f);
                image_2.setX(image_2.getX() + x * 10 / 2.5f);

                float x1_1 = image_1.getX();
                float x1_2 = image_1.getWidth() + image_1.getX();

                float x2_1 = image_2.getX();
                float x2_2 = image_2.getWidth() + image_2.getX();

                Display display = getActivity().getWindowManager().getDefaultDisplay();
                int width = display.getWidth();  // deprecated
                int height = display.getHeight();  // deprecated
                //System.out.println( width + " " + x2_1 + " " + x2_2);

                if ( x1_1 > 0 ){
                    image_2.setX(x1_1 - image_2.getWidth());
                }
                if ( x2_1 > 0 ){
                    image_1.setX(x2_1 - image_1.getWidth());
                }

                if (x1_2 < width){
                    image_2.setX(x1_2);
                }
                if (x2_2 < width){
                    image_1.setX(x2_2);
                }
            }
        });

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume(){
        super.onResume();
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this,
                sensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause(){
        super.onPause();
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.unregisterListener(this, sensor);
    }
}
