package com.example.qwerty.timemanager;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import TImeManagerDataBase.TimePeriodDBTableEntry;
import TImeManagerDataBase.UserActivityDB;
import TImeManagerDataBase.UserActivityDBTableEntry;

/**
 * This timer interacts with the service's timer from an activity's context.
 */
public class ActivityTimer {
    /**
     * Updates the timer textView on the activity this timer works on
     */
    UIApdater timerTextViewSetter;
    /**
     * Context of the activity where this timer is used
     */
    Context activityContext;
    /**
     * Represents the current session number
     */
    SessionNumber currentSessionNumber;
    /**
     * connection to the service timer
     */
    private ServiceConnection timerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            TimerService.LocalBinder binder = (TimerService.LocalBinder) service;
            timerService = binder.getService();
            timerServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            timerServiceBound = false;
        }
    };
    /**
     * the service that manages the main timer
     */
    private TimerService timerService;
    /**
     * indicates whether timer service is bound or not
     */
    private boolean timerServiceBound;
    /**
     *  time span between two events: 1)start button clicked 2)stop button clicked
     *  This object is stored in the application data, so it works correct when the app
     *  is in sleep
     */
    private TimeSpan startStopPeriod;
    /**
     * Takes time from the service and displays it on the screen.
     */
    private Handler getTimeHandler;
    /**
     * contains timers that need to maintain their values when the the phone sleeps
     */
    private ApplicationData appState;

    /**
     * Constructor
     * @param activityContext Context of the activity where this timer is used
     */
    public ActivityTimer(Context activityContext, UIApdater timerTextViewSetter) {
        this.activityContext = activityContext;
        this.timerTextViewSetter = timerTextViewSetter;

        getTimeHandler = new Handler();

        appState = ((ApplicationData)activityContext.getApplicationContext());

        startStopPeriod = appState.getStartStopPeriod();

        currentSessionNumber = SessionNumber.getInstance();
    }

    /**
     * Bind timer to TimerService. The service must be started beforehand.
     * @see TimerService
     */
    public void bindTimer() {
        final Intent timerServiceIntent = new Intent(activityContext, TimerService.class);
        activityContext.bindService(timerServiceIntent, timerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Start TimerService
     * @see TimerService
     */
    public void startService() {
        Intent serviceIntent = new Intent(activityContext, TimerService.class);
        activityContext.startService(serviceIntent);
    }

    /**
     * Unbind this object from TimerService
     * @see TimerService
     */
    public void unbindTimer() {
        activityContext.unbindService(timerServiceConnection);
    }

    /**
     * Increase the amount of seconds passed by the provided value.
     * @param secondsToAdd the amount of seconds to add
     */
    public void addSeconds(long secondsToAdd) {
        if (timerService != null)
            timerService.addSeconds(secondsToAdd);
    }

    /**
     * set the amount of second passed. The service's timer will now count seconds
     * starting from the argument's value
     * @param seconds the amount of second
     */
    public void setSecondsPassed(long seconds) {
        timerService.setSecondsPassed(seconds);
    }

    /**
     * Called when startTimerButton is clicked. Starts the service's timer.
     */
    public void startTimer() {
        if (timerServiceBound) {
            timerService.startTimer();

            int startWhen = 0;
            int period = 1000;

            startStopPeriod.setStartDate(Calendar.getInstance());

            getTimeHandler.postDelayed(displayTime, period);
        }
    }

    /**
     * Gets current time period from the timer service and displays on the screen
     */
        Runnable displayTime = new Runnable() {
            @Override
            public void run() {
                long secs = timerService.getSecondsPassed();

                String timeString = getFormattedTimeString(secs);

                setTimerViewText(timeString);

                getTimeHandler.postDelayed(displayTime, 1000);
            }
        };

    private void setTimerViewText(final String text) {
        timerTextViewSetter.setTextViewText(text);
    }

    /**
     * Creates a user friendly time string - h:m:s
     * @param secs seconds
     * @return a user friendly time string
     */
    private String getFormattedTimeString(long secs) {
        long hours = TimeUnit.SECONDS.toHours(secs);
        long minutes = TimeUnit.SECONDS.toMinutes(secs) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.SECONDS.toSeconds(secs) - TimeUnit.HOURS.toSeconds(hours) -
                TimeUnit.MINUTES.toSeconds(minutes);

        String time = String.format("%02d : %02d : %02d", hours, minutes, seconds);

        return time;
    }

    public void stopTimer() {
        if (timerService != null)
            timerService.stopTimer();
    }

    /**
     * Called when startTimerButton is clicked.
     * Stops the service's timer
     * @param userActivityEntry user's activity
     */
    public void stopTimer(UserActivityDBTableEntry userActivityEntry) {
        if (userActivityEntry == null) {
            Log.e("stopTimer:", "userActivityEntry = null");
            return;
        }

        if (timerService != null) {
            timerService.stopTimer();
        } else {
            return;
        }

        getTimeHandler.removeCallbacks(displayTime);

        UserActivityDB db = UserActivityDB.getInstance();

        startStopPeriod.setEndDate(Calendar.getInstance());

        long secondsPassedSinceTimerStarted = startStopPeriod.getDuration(TimeUnit.SECONDS);
        TimePeriodDBTableEntry timePeriodEntry = new TimePeriodDBTableEntry(new Date(), secondsPassedSinceTimerStarted);
        timePeriodEntry.setSessionNumber(currentSessionNumber.getCurrentSessionNumber());

        timePeriodEntry.setIdUserActivity(userActivityEntry.getId());

        db.addNewTimePeriod(timePeriodEntry);

        startStopPeriod.reset();
    }
}
