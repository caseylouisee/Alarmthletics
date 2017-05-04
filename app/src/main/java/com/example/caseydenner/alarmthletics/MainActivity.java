package com.example.caseydenner.alarmthletics;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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

    private CheckBox weekdays;
    private CheckBox weekends;
    private CheckBox everyday;
    private CheckBox specific;
    private CheckBox monday;
    private CheckBox tuesday;
    private CheckBox wednesday;
    private CheckBox thursday;
    private CheckBox friday;
    private CheckBox saturday;
    private CheckBox sunday;

    Boolean bweekdays;

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
        weekdays = (CheckBox) findViewById(R.id.chk_weekdays);
        weekends = (CheckBox) findViewById(R.id.chk_weekends);
        everyday = (CheckBox) findViewById(R.id.chk_everyday);
        specific = (CheckBox) findViewById(R.id.chk_specific);
        monday = (CheckBox) findViewById(R.id.chk_monday);
        tuesday = (CheckBox) findViewById(R.id.chk_tuesday);
        wednesday = (CheckBox) findViewById(R.id.chk_wednesday);
        thursday = (CheckBox) findViewById(R.id.chk_thursday);
        friday = (CheckBox) findViewById(R.id.chk_friday);
        saturday = (CheckBox) findViewById(R.id.chk_saturday);
        sunday = (CheckBox) findViewById(R.id.chk_sunday);

        monday.setVisibility(View.INVISIBLE);
        tuesday.setVisibility(View.INVISIBLE);
        wednesday.setVisibility(View.INVISIBLE);
        thursday.setVisibility(View.INVISIBLE);
        friday.setVisibility(View.INVISIBLE);
        saturday.setVisibility(View.INVISIBLE);
        sunday.setVisibility(View.INVISIBLE);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
    }

    public void onChecked(View view){
        if(weekdays.isChecked()){
            weekdays.setChecked(true);
        }
        if(weekends.isChecked()){
            weekends.setChecked(true);
        }
        if(everyday.isChecked()){
            weekdays.setChecked(true);
            weekends.setChecked(true);
        } else if(!everyday.isChecked()){
            weekdays.setChecked(false);
            weekends.setChecked(false);
        }
        if(weekdays.isChecked()&&weekends.isChecked()){
            everyday.setChecked(true);
        }
        if(specific.isChecked()){
            weekdays.setVisibility(View.INVISIBLE);
            weekends.setVisibility(View.INVISIBLE);
            everyday.setVisibility(View.INVISIBLE);

            monday.setVisibility(View.VISIBLE);
            tuesday.setVisibility(View.VISIBLE);
            wednesday.setVisibility(View.VISIBLE);
            thursday.setVisibility(View.VISIBLE);
            friday.setVisibility(View.VISIBLE);
            saturday.setVisibility(View.VISIBLE);
            sunday.setVisibility(View.VISIBLE);
        } else if (!specific.isChecked()) {
            weekdays.setVisibility(View.VISIBLE);
            weekends.setVisibility(View.VISIBLE);
            everyday.setVisibility(View.VISIBLE);

            monday.setVisibility(View.INVISIBLE);
            tuesday.setVisibility(View.INVISIBLE);
            wednesday.setVisibility(View.INVISIBLE);
            thursday.setVisibility(View.INVISIBLE);
            friday.setVisibility(View.INVISIBLE);
            saturday.setVisibility(View.INVISIBLE);
            sunday.setVisibility(View.INVISIBLE);
        }
    }


    public void onToggleClicked(View view) {
        if(toggleButton.isChecked()){
            if(weekdays.isChecked()){
                setAlarm(Calendar.MONDAY);
                setAlarm(Calendar.TUESDAY);
                setAlarm(Calendar.WEDNESDAY);
                setAlarm(Calendar.THURSDAY);
                setAlarm(Calendar.FRIDAY);
            }
            if(weekends.isChecked()){
                setAlarm(Calendar.SATURDAY);
                setAlarm(Calendar.SUNDAY);
            }
            if(everyday.isChecked()){
                setAlarm(Calendar.MONDAY);
                setAlarm(Calendar.TUESDAY);
                setAlarm(Calendar.WEDNESDAY);
                setAlarm(Calendar.THURSDAY);
                setAlarm(Calendar.FRIDAY);
                setAlarm(Calendar.SATURDAY);
                setAlarm(Calendar.SUNDAY);
            }
            if(specific.isChecked()){
                if(monday.isChecked()){
                    setAlarm(Calendar.MONDAY);
                }
                if(tuesday.isChecked()){
                    setAlarm(Calendar.TUESDAY);
                }
                if(wednesday.isChecked()){
                    setAlarm(Calendar.WEDNESDAY);
                }
                if(thursday.isChecked()){
                    setAlarm(Calendar.THURSDAY);
                }
                if(friday.isChecked()){
                    setAlarm(Calendar.FRIDAY);
                }
                if(saturday.isChecked()){
                    setAlarm(Calendar.SATURDAY);
                }
                if(sunday.isChecked()){
                    setAlarm(Calendar.SUNDAY);
                }
            }
        }
    }

    public void setAlarm(int day){
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        Calendar now = Calendar.getInstance();
        Calendar alarm = Calendar.getInstance();
        alarm.set(Calendar.HOUR_OF_DAY, hour);
        alarm.set(Calendar.MINUTE, minute);
        alarm.set(Calendar.DAY_OF_WEEK, day);

//        if (now.after(alarm)) {
//            Log.d("OnToggleClicked","Added a day");
//            alarm.add(Calendar.DATE, 1);
//        }

        Log.d("Time", alarm.toString());
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), 24 * 7 * 60 * 60 * 1000, pendingIntent);
        textView.setText("Alarm on");
        Log.d("onToggleClicked", "Alarm On");

    }

    public void setTextViewText(String string){
        textView.setText(string);
    }
}
