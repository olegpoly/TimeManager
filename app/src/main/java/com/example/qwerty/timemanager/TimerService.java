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
    /**
     * Binder given to clients
     */
    private final IBinder mBinder = new LocalBinder();
    /**
     * Timer that counts time. It's timer task increments the counter variable every time
     * it is run.
     */
    private Timer timeCounterTimer;
    /**
     * The counter variable. Represents the number of seconds passed.
     */
    private long secPassed = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Starts the service's timer.
     */
    public void startTimer() {
        if (timeCounterTimer != null) {
            return;
        }

        timeCounterTimer = new Timer();
        // timer ticks every second, starting one second after it's scheduled.
        timeCounterTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                secPassed++;
            }
        }, 1000, 1000);
    }

    /**
     * Stops the service's timer.
     */
    public void stopTimer() {
        if (timeCounterTimer != null)
            timeCounterTimer.cancel();
        timeCounterTimer = null;
    }

    /**
     * @return the amount of seconds passed
     */
    public long getSecondsPassed() {
        return secPassed;
    }

    /**
     * @param seconds set the amount of second passed. The service's timer will now count seconds
     * starting from this value.
     */
    public void setSecondsPassed(long seconds) {
        secPassed = seconds;
    }

    /**
     * Increase the amount of seconds passed by the provided value.
     * @param secondsToAdd the amount of seconds to add
     */
    public void addSeconds(long secondsToAdd) {
        secPassed += secondsToAdd;
    }

    /**
     * Class used for the client Binder.
     */
    public class LocalBinder extends Binder {
        TimerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TimerService.this;
        }
    }

}
