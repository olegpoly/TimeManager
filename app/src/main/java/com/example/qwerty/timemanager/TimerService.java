package com.example.qwerty.timemanager;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Oleg on 11/20/2014.
 */
public class TimerService extends Service {
    private final IBinder mBinder = new LocalBinder();  // Binder given to clients
    private Timer timeCounterTimer;
    private long secPassed = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * method for clients
     */
    public void startTimer() {
        if (timeCounterTimer != null) {
            return;
        }

        timeCounterTimer = new Timer();
        timeCounterTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                secPassed++;
            }
        }, 1000, 1000);
    }

    public void stopTimer() {
        if (timeCounterTimer != null)
            timeCounterTimer.cancel();
        timeCounterTimer = null;
    }

    public long getTime() {
        return secPassed;
    }

    public void setTime(long seconds) {
        secPassed = seconds;
    }

    public void addTime(long secondsToAdd) {
        secPassed += secondsToAdd;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        TimerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TimerService.this;
        }
    }

}
