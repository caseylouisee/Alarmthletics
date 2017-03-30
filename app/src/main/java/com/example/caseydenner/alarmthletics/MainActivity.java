package com.example.caseydenner.alarmthletics;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private ToggleButton toggleButton;
    private TextView textView;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static MainActivity inst;


    public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        toggleButton = (ToggleButton) findViewById(R.id.alarmToggle);
        textView = (TextView) findViewById(R.id.textView);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
    }


    public void onToggleClicked(View view) {
        if(toggleButton.isChecked()){
            int hour = timePicker.getCurrentHour();
            int minute = timePicker.getCurrentMinute();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            Log.d("Time", calendar.toString());

            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
              //      AlarmManager.INTERVAL_DAY, pendingIntent);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            textView.setText("Alarm on");

            Log.d("onToggleClicked", "Alarm On");

        }
    }

    public void setTextViewText(String string){
        textView.setText(string);
    }
}
