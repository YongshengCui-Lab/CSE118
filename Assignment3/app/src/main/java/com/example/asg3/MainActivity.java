package com.example.asg3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import de.greenrobot.event.EventBus;
import com.example.asg3.MyService.MyBinder;

public class MainActivity extends AppCompatActivity {
    boolean bool = true;
    private int TIME = 30000;
    private boolean isBound;
    private boolean moved_bool;
    private MyBinder binder;
    private MyService myService;
    private Long dateMoved_long = 0L;
    private AtomicLong firstAccTime_atomicLong;

    private ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName className, IBinder serviceBinder)
        {
            binder = (MyBinder) serviceBinder;
            myService = binder.getService();
            isBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0)
        {
            isBound = false;
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(bool == true) {
            setContentView(R.layout.activity_main);
        }else{
            setContentView(R.layout.activity_main_2);
        }
        isBound = false;
        moved_bool = false;
        firstAccTime_atomicLong = null;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        Intent intent = new Intent(MainActivity.this, MyService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
    public boolean didItMove(ServiceResult tempRes)
    {
        Date date = new Date();
        firstAccTime_atomicLong = tempRes.lngValue;
        if (tempRes.lngValue != null && !moved_bool) {
            if (date.getTime() - firstAccTime_atomicLong.longValue() > TIME) {
                dateMoved_long = firstAccTime_atomicLong.longValue();
                moved_bool = true;
            }
        }
        return moved_bool;
    }
    public void Time()
    {
        String currentTime = myService.getCurrentTime();
        TextView status_textView = findViewById(R.id.status_textView);
        status_textView.setText(currentTime);

    }
    private void ServiceCon() {
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onPause() {
        super.onPause();

        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }

    }
    public void onClearClick(View v) {
        myService.clearTask();
        moved_bool = false;
        firstAccTime_atomicLong = null;
    }
    public void onExitClick(View v) {
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
            Intent intent = new Intent(this, MyService.class);
            stopService(intent);
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        System.exit(0);

    }
    public void onEventMainThread(ServiceResult result) {

        TextView textView;
        if(bool == true)
            textView = findViewById(R.id.status_textView);
        else
            textView = findViewById(R.id.status_view);

        Boolean move = didItMove(result);
        Long countTime = new Date().getTime() - result.startValue;
        if (move) {
            Long seconds = ((new Date().getTime()) - dateMoved_long) / 1000;
            Long minutes = seconds / 60;
            Long minSeconds = seconds - minutes * 60;

            if (seconds < 60) {
                textView.setText("The phone moved\n" + Long.toString(seconds) + " seconds ago");
            } else {
                textView.setText("The phone moved\n" + Long.toString(minutes) + " minute and " + Long.toString(minSeconds) + " second");
            }
        } else if (countTime < TIME) {
            Long seconds = (TIME - countTime) / 1000;
            textView.setText("App will start detecting in\n" + Long.toString(seconds) + " second");
        } else {
            Long sec = (countTime - TIME) / 1000;
            Long mins = sec / 60;
            Long minSeconds = sec - mins * 60;

            if (sec <60) {
                textView.setText("Hasn’t budged an inch \n" + Long.toString(sec) + " second");
            } else {
                textView.setText("Hasn’t budged an inch \n" + Long.toString(mins) + " minute and " + Long.toString(minSeconds) + " second");
            }
        }
    }
}
