package com.example.caseydenner.alarmthletics;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.camera2.CaptureRequest.SENSOR_SENSITIVITY;

/**
 * Created by caseydenner on 30/03/2017.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    MainActivity inst = MainActivity.instance();

    private SensorManager mSensorManager;
    private Sensor proximitySensor;

    AlertDialog.Builder builder;
    AlertDialog dialog;

    ArrayList<String> actions = new ArrayList<>();
    Random rand = new Random();
    //int random  = rand.nextInt(actions.size());

    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("OnRecieve", "Alarm Reciever");
        inst.setTextViewText("Alarm triggered");

        mSensorManager = (SensorManager) inst.getSystemService(SENSOR_SERVICE);
        proximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        actions.add(0,"Do 10 sit ups");
        actions.add(1,"Do 5 press ups");

        int test = 1;

        builder = new AlertDialog.Builder(inst);
        builder.setMessage(actions.get(test)).setTitle("Wake up you fat git");
        dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();

        if(test == 0){
            //Sit up check
        } else if (test == 1){
            Log.d("Test", "test == 1");
            PressUpCheck(ringtone);
        }

    }

    public void PressUpCheck(final Ringtone ringtone){
        Log.d("PressUpCheck", "Method called");

        SensorEventListener sensorEventListener = new SensorEventListener() {
            int count = 0;
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    if (sensorEvent.values[0] == 0) {
                        if(count == 4){
                            Log.d("PressUpCheck", "5 Press ups completed");
                            dialog.dismiss();
                            ringtone.stop();
                        }
                        count ++;
                        Log.d("PressUpCheck", "sensorValues = 0");
                        dialog.setMessage(count + " Press Ups done");
                        dialog.setTitle("Keep going!");
                    } else {
                        Log.d("PressUpCheck", "sensorValues not 0");
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        mSensorManager.registerListener(sensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d("PressUpCheck", "Listerner registered");

    }
}
