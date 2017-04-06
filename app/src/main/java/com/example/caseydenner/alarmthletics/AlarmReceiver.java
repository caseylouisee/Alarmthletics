package com.example.caseydenner.alarmthletics;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private Sensor accelerometerSensor;

    private float[] startPosition = new float[3];
    private final int NEGATIVE_SENSOR_LIMIT = -5;
    private final int POSITIVE_SENSOR_LIMIT = 5;
    private final int SENSOR_OFFSET = 8;
    private int repetitions = 5;
    private boolean initialisation = false;

    AlertDialog.Builder builder;
    AlertDialog dialog;

    ArrayList<String> actions = new ArrayList<>();
    Random rand = new Random();

    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("OnRecieve", "Alarm Reciever");
        inst.setTextViewText("Alarm triggered");

        mSensorManager = (SensorManager) inst.getSystemService(SENSOR_SERVICE);
        proximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        actions.add(0,"Do 5 sit ups");
        actions.add(1,"Do 5 press ups");

        //int random  = rand.nextInt(actions.size());
        int random = 0;

        builder = new AlertDialog.Builder(inst);
        builder.setMessage(actions.get(random)).setTitle("Wake up you fat git");
        dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();

        if(random == 0){
            SitUpCheck(ringtone);
        } else if (random == 1){
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
                        count++;
                        if(count == 5){
                            Log.d("PressUpCheck", "5 Press ups completed");
                            dialog.dismiss();
                            ringtone.stop();
                        } else {
                            Log.d("PressUpCheck", count + "PressUps" + " sensorValues = 0");
                            dialog.setMessage(count + " Press Ups done");
                            dialog.setTitle("Keep going!");
                        }
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
        Log.d("PressUpCheck", "Listener registered");
        
    }

    public void SitUpCheck (final Ringtone ringtone){
        Log.d("SitUpCheck", "Method called");
        // If in ready position
        SensorEventListener sensorEventListener = new SensorEventListener() {
            int count = 0;
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                    if (!initialisation) {
                        startPosition[0] = sensorEvent.values[0];
                        startPosition[1] = sensorEvent.values[1];
                        startPosition[2] = sensorEvent.values[2];
                        initialisation = true;
                    }
                    if (count < (repetitions * 2)) {
                        if (startPosition[2] < NEGATIVE_SENSOR_LIMIT) {
                            if (sensorEvent.values[2] < startPosition[2] + SENSOR_OFFSET) {
                                count = counter(sensorEvent, count, 2);
                            }
                        } else if (startPosition[2] > POSITIVE_SENSOR_LIMIT) {
                            if (sensorEvent.values[2] < startPosition[2] - SENSOR_OFFSET) {
                                count = counter(sensorEvent, count, 2);
                            }
                        } else if (startPosition[1] < NEGATIVE_SENSOR_LIMIT) {
                            if (sensorEvent.values[1] < startPosition[1] + SENSOR_OFFSET) {
                                count = counter(sensorEvent, count, 1);
                            }
                        } else if (startPosition[1] > POSITIVE_SENSOR_LIMIT) {
                            if (sensorEvent.values[1] < startPosition[1] - SENSOR_OFFSET) {
                                count = counter(sensorEvent, count, 1);
                            }
                        }
                    } else if(count == repetitions*2){
                        Log.d("SitUpCheck", "5 Sit ups completed");
                        dialog.dismiss();
                        ringtone.stop();
                        count++;
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        mSensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public int counter(SensorEvent event,int counter,int axis){
        startPosition[0] = event.values[0];
        startPosition[1] = event.values[1];
        startPosition[2] = event.values[2];
        counter++;
        Log.d("SitUpCheck", "sensorValues = " + event.values[axis]);
        if(counter%2==0){
            Log.d("SitUpCheck", counter/2 + "SitUps");
            dialog.setMessage(counter/2 + " Sit ups done");
        }
        dialog.setTitle("Keep going!");
        return counter;
    }
}
