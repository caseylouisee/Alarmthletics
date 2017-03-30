package com.example.caseydenner.alarmthletics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    TimePicker time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = (TimePicker)findViewById(R.id.timePicker);

        //Testing push

    }



}
