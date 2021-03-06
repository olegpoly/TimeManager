package com.example.qwerty.timemanager;

import android.app.Application;
import android.content.Context;

/**
 * Holds data that is needed to maintain it's state through all app's states.
 */
public class ApplicationData extends Application {
    /**
     * this variable is used in the main activity for saving
     * time span between two events: 1)start button click 2)stop button click
     */
    TimeSpan startStopPeriod;
    /**
     * The service's timer is suspended during sleep, this variable represents the time spent is sleep.
     */
    TimeSpan timeInSleep;
    /**
     * Application's context
     */
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize variables with standard values
        startStopPeriod = new TimeSpan();
        timeInSleep = new TimeSpan();
        appContext = getApplicationContext();
    }

    /**
     * Get application's context
     * @return application's context
     */
    public static Context getAppContext() {
        return appContext;
    }

    /**
     * @return the startStopPeriod variable
     */
    public TimeSpan getStartStopPeriod() {
        return startStopPeriod;
    }

    /**
     * @param startStopPeriod
     */
    public void setStartStopPeriod(TimeSpan startStopPeriod) {
        this.startStopPeriod = startStopPeriod;
    }

    /**
     * @return
     */
    public TimeSpan getTimeInSleep() {
        return timeInSleep;
    }
}