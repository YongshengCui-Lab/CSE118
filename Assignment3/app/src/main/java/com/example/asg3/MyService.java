package com.example.asg3;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {
    private NotificationManager notificationManager;
    private int ONGOING_NOTIFICATION_ID = 1;
    private Thread myThread;
    private MyServiceTask myTask;
    private PowerManager.WakeLock wakeLock;
    private final IBinder myBinder = new MyBinder();

    public class MyBinder extends Binder {
        MyService getService()
        {
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return myBinder;
    }

    public MyService() {
    }
    @Override
    public void onCreate()
    {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        myTask = new MyServiceTask(getApplicationContext());
        myThread = new Thread(myTask);
        myThread.start();
    }

    public void clearTask()
    {
        myTask.clearMyServiceTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag:");
        if (!myThread.isAlive()) {
            myThread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        wakeLock.release();
    }
    public String getCurrentTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        return (dateFormat.format(new Date()));
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }
}