package com.developer.andrew.startappwithfcm.service;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.developer.andrew.startappwithfcm.activity.AlarmActivity;

public class AlarmService extends IntentService {

    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sendNotification("Wake up! Wake up");

        if (intent != null) {
            Intent dialogIntent = new Intent(getBaseContext(), AlarmActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(dialogIntent);
        }
    }

    private void sendNotification(String message) {
        Log.d("AlarmService", "Preparing to send notification...: " + message);
        alarmNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, AlarmActivity.class), 0);

        // 建立NotificationCompat.Builder物件
        NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(this);

        // 設定小圖示、大圖示、狀態列文字、時間、內容標題、內容訊息和內容額外資訊
        alarmNotificationBuilder.setSmallIcon(android.R.drawable.arrow_up_float)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Basic Notification")
                .setContentText("Demo for basic notification control.")
                .setContentInfo("3");

        alarmNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alarmNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }
}