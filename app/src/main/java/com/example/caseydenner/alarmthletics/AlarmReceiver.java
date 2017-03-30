package com.example.caseydenner.alarmthletics;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by caseydenner on 30/03/2017.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("OnRecieve", "Alarm Reciever");
        MainActivity inst = MainActivity.instance();
        inst.setTextViewText("Alarm triggered");

        AlertDialog.Builder builder = new AlertDialog.Builder(inst);
        builder.setMessage("Do 10 sit ups").setTitle("Wake up you fat git");
        AlertDialog dialog = builder.create();
        dialog.show();

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();
    }
}
