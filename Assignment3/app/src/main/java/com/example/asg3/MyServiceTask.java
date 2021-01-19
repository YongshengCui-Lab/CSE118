package com.example.asg3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import de.greenrobot.event.EventBus;

public class MyServiceTask implements Runnable {


    private boolean running;
    private Context context;

    private int CAP_TIME = 30000;

    private Long T1 = 0L;
    private Long T0;
    private float x;
    private float y;
    private AtomicLong first_accel_time;

    public MyServiceTask(Context _context) {
        context = _context;
        T0 = new Date().getTime();
    }
    @Override
    public void run() {
        running = true;
        while (running) {
            ((SensorManager) context.getSystemService(Context.SENSOR_SERVICE)).registerListener(
                    new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent event) {
                             x = -event.values[0];
                             y = event.values[1];
                            if (Math.abs(y) > .5 || Math.abs(x) > .5) {
                                T1 = new Date().getTime();
                                if (T1 - T0 > CAP_TIME) {
                                    first_accel_time = new AtomicLong(T1);
                                }
                            }
                        }
                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {
                        }
                    },
                    ((SensorManager) context.getSystemService(Context.SENSOR_SERVICE)).getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_GAME);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
            ServiceResult result = new ServiceResult();
            result.lngValue = first_accel_time;
            result.startValue = T0;
            EventBus.getDefault().post(result);
        }
    }
    public void setTaskState(boolean b) {
    }
    public void stopProcessing() {
        running = false;
    }


    public void clearMyServiceTask(){
        T1 = 0L;
        T0 = new Date().getTime();
        first_accel_time = null;
    }
}
