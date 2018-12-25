package com.developer.andrew.startappwithfcm.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.developer.andrew.startappwithfcm.MyConstants;
import com.developer.andrew.startappwithfcm.R;
import com.developer.andrew.startappwithfcm.receiver.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by Admin on 6/15/2016.
 */
public class AlarmActivity extends FragmentActivity implements View.OnClickListener {

    private static int timeHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private static int timeMinute = Calendar.getInstance().get(Calendar.MINUTE);

    TextView textViewTime;
    private static TextView textViewMessage;
    private static AlarmActivity inst;

    public static AlarmActivity instance() {
        return inst;
    }

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewTime.setText(timeHour + ":" + timeMinute);

        textViewMessage = (TextView) findViewById(R.id.message);

        Button btnStart = (Button) findViewById(R.id.btnStart);
        Button btnStop = (Button) findViewById(R.id.btnStop);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnStart) {
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(AlarmActivity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, intent, 0);
            setTextViewMessage("");
            Bundle bundle = new Bundle();
            bundle.putInt(MyConstants.HOUR, timeHour);
            bundle.putInt(MyConstants.MINUTE, timeMinute);
            MyDialogFragment fragment = new MyDialogFragment(new MyHandler());
            fragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(fragment, MyConstants.TIME_PICKER);
            transaction.commit();
        } else if (v.getId() == R.id.btnStop) {
            cancelAlarm();
            setTextViewMessage("");
            Log.d("MyActivity", "Alarm Off");
        }
    }

    class MyHandler extends Handler {
        public MyHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            timeHour = bundle.getInt(MyConstants.HOUR);
            timeMinute = bundle.getInt(MyConstants.MINUTE);
            textViewTime.setText(timeHour + ":" + timeMinute);
            setAlarm();
        }
    }

    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timeHour);
        calendar.set(Calendar.MINUTE, timeMinute);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        if (null != alarmManager) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public void setTextViewMessage(String text) {
        textViewMessage.setText(text);
    }
}