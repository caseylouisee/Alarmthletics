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

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by caseydenner on 30/03/2017.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    MainActivity inst = MainActivity.instance();

    private SensorManager mSensorManager;
    private Sensor proximitySensor;
    private Sensor accelerometerSensor;
    private Sensor linearAccelerometerSensor;

    private float[] startPosition = new float[3];
    private final int POSITIVE_ACCELEROMETER_CHECK_VALUE = 5;
    private final int NEGATIVE_ACCELEROMETER_CHECK_VALUE = -5;
    private final int NEGATIVE_SENSOR_LIMIT = -5;
    private final int POSITIVE_SENSOR_LIMIT = 5;
    private final int SENSOR_OFFSET = 8;
    private int repetitions = 5;
    private boolean initialisation = false;
    private boolean downSwitch = true;

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
        linearAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        actions.add(0,"Do 5 sit ups");
        actions.add(1,"Do 5 press ups");
        actions.add(2,"Do 5 squats");

        //int random  = rand.nextInt(actions.size());
        int random = 2;

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
        } else if(random==2){
            SquatCheck(ringtone);
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
                    if (count < (repetitions*2)) {
                        if (startPosition[2] < NEGATIVE_SENSOR_LIMIT) {
                            if (sensorEvent.values[2] > startPosition[2] + SENSOR_OFFSET) {
                                count = sitUpCounter(sensorEvent, count, 2);
                            }
                        } else if (startPosition[2] > POSITIVE_SENSOR_LIMIT) {
                            if (sensorEvent.values[2] < startPosition[2] - SENSOR_OFFSET) {
                                count = sitUpCounter(sensorEvent, count, 2);
                            }
                        } else if (startPosition[1] < NEGATIVE_SENSOR_LIMIT) {
                            if (sensorEvent.values[1] > startPosition[1] + SENSOR_OFFSET) {
                                count = sitUpCounter(sensorEvent, count, 1);
                            }
                        } else if (startPosition[1] > POSITIVE_SENSOR_LIMIT) {
                            if (sensorEvent.values[1] < startPosition[1] - SENSOR_OFFSET) {
                                count = sitUpCounter(sensorEvent, count, 1);
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

    public void SquatCheck (final Ringtone ringtone){
        Log.d("SquatCheck", "Method called");
        // If in ready position
        SensorEventListener sensorEventListener = new SensorEventListener() {
            int count = 0;
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
                    if (count < (repetitions * 2)) {
                        if (downSwitch) {
                            if (sensorEvent.values[0] < NEGATIVE_ACCELEROMETER_CHECK_VALUE) {
                                count++;
                            } else if (sensorEvent.values[1] < NEGATIVE_ACCELEROMETER_CHECK_VALUE) {
                                count++;
                            } else if (sensorEvent.values[2] < NEGATIVE_ACCELEROMETER_CHECK_VALUE) {
                                count++;
                            }
                            downSwitch = !downSwitch;
                        }else {
                            if (sensorEvent.values[0] > POSITIVE_ACCELEROMETER_CHECK_VALUE) {
                                count++;
                            } else if (sensorEvent.values[1] > POSITIVE_ACCELEROMETER_CHECK_VALUE) {
                                count++;
                            } else if (sensorEvent.values[2] > POSITIVE_ACCELEROMETER_CHECK_VALUE) {
                                count++;
                            }
                            downSwitch = !downSwitch;
                        }
                        Log.d("SquatCheck", sensorEvent.values.toString());
                    } else if(count == (repetitions*2)){
                        Log.d("SquatCheck", "5 Squats completed");
                        dialog.dismiss();
                        ringtone.stop();
                        count++;
                    }

                    if (count%2 == 0 && count <= (repetitions*2)){
                        dialog.setMessage((count/2) + " Squats done");
                        dialog.setTitle("Keep going!");
                        Log.d("SquatCheck", count/2 + "Squats");
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        mSensorManager.registerListener(sensorEventListener, linearAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public int sitUpCounter(SensorEvent event,int counter,int axis){
        startPosition[0] = event.values[0];
        startPosition[1] = event.values[1];
        startPosition[2] = event.values[2];
        counter++;
        Log.d("SitUpCheck", "sensorValues = " + event.values[axis]);
        if(counter%2 == 0) {
            dialog.setTitle("Keep going!");
            dialog.setMessage(counter/2 + " Sit ups done");
            Log.d("SitUpCheck", counter/2 + "SitUps");
        }
        return counter;
    }
}
