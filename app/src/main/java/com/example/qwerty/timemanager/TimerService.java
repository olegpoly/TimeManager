package com.example.qwerty.timemanager;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * The service that manges the main timer. This timer must work when the app is active and
 * when the app is on the background. When the phone is in sleep and this time doesn't work,
 * the app uses different mechanisms to keep track of time passed.
 */
public class TimerService extends Service {
    /**
     * Binder given to clients
     */
    private final IBinder mBinder = new LocalBinder();
    /**
     * the handler that is used for timer ticks, where tick is a runnable task
     */
    private Handler timerHandler = new Handler();
    /**
     * represents one tick of a timer
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            secPassed += 1;
            timerHandler.postDelayed(mUpdateTimeTask, 1000);
        }
    };
    /**
     * true if timer is ticking, false otherwise
     */
    boolean timerWorking = false;
    /**
     * The counter variable. Represents the number of seconds passed.
     */
    private long secPassed = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start();
        return (START_NOT_STICKY);
    }

    private void start() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        // Build notification
        Notification note = new NotificationCompat.Builder(this)
                .setContentTitle("TimerManager")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText("TimerManager is on")
                .setContentIntent(pIntent).build();

        startForeground(1337, note);
    }

    /**
     * Starts the service's timer.
     */
    public void startTimer() {
        // can't run two timers on the same instance at once
        if (timerWorking == true) {
            return;
        }

        // start the timer
        timerHandler.postDelayed(mUpdateTimeTask, 100);
        timerWorking = true;
    }

    /**
     * Stops the service's timer.
     */
    public void stopTimer() {
        // stop the timer
        timerHandler.removeCallbacks(mUpdateTimeTask);
        timerWorking = false;
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
