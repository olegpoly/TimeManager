package com.example.qwerty.timemanager;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import TImeManagerDataBase.TimePeriodDBTableEntry;
import TImeManagerDataBase.UserActivityDB;
import TImeManagerDataBase.UserActivityDBTableEntry;

/**
 * This timer interacts with the service's timer from an activity's context.
 */
public class ActivityTimer {
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
    private Timer setPassedTimeTimer;
    /**
     * TimerTask that displays time on the screen when called
     */
    private TimerTask displayTime = createDisplayTimeTimerTask();
    /**
     * contains timers that need to maintain their values when the the phone sleeps
     */
    private ApplicationData appState;

    /**
     * Constructor
     * @param activityContext Context of the activity where this timer is used
     */
    public ActivityTimer(Context activityContext) {
        this.activityContext = activityContext;

        final Intent timerServiceIntent = new Intent(activityContext, TimerService.class);
        activityContext.bindService(timerServiceIntent, timerServiceConnection, Context.BIND_AUTO_CREATE);

        appState = ((ApplicationData)activityContext.getApplicationContext());

        startStopPeriod = appState.getStartStopPeriod();

        currentSessionNumber = SessionNumber.getInstance();
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

            if (setPassedTimeTimer != null) return;

            setPassedTimeTimer = new Timer();
            int startWhen = 0;
            int period = 1000;

            startStopPeriod.setStartDate(Calendar.getInstance());
            displayTime = createDisplayTimeTimerTask();

            setPassedTimeTimer.schedule(displayTime, startWhen, period);
        }
    }

    /**
     * Gets current time period from the timer service and displays on the screen
     */
    private TimerTask createDisplayTimeTimerTask() {
        TimerTask displayTime = new TimerTask() {
            @Override
            public void run() {
                long secs = timerService.getSecondsPassed();

                String timeString = getFormattedTimeString(secs);

                setTimerViewText(timeString);
            }
        };

        return displayTime;
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

    /**
     * Called when startTimerButton is clicked.
     * Stops the service's timer
     * @param userActivityEntry user's activity
     */
    public void stopTimer(UserActivityDBTableEntry userActivityEntry) {
        if (timerService != null)
            timerService.stopTimer();

        if (setPassedTimeTimer != null) {
            setPassedTimeTimer.cancel();
            setPassedTimeTimer.purge();
            setPassedTimeTimer = null;
        }

        UserActivityDB db = UserActivityDB.getInstance(activityContext);

        startStopPeriod.setEndDate(Calendar.getInstance());

        long secondsPassedSinceTimerStarted = startStopPeriod.getDuration(TimeUnit.SECONDS);
        TimePeriodDBTableEntry tp = new TimePeriodDBTableEntry(new Date(), secondsPassedSinceTimerStarted);
        tp.setSessionNumber(currentSessionNumber.getCurrentSessionNumber());

        tp.setIdUserActivity(userActivityEntry.getId());

        db.addNewTimePeriod(tp);

        startStopPeriod.reset();
    }
}
