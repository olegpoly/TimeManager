package com.example.qwerty.timemanager;

import android.app.Application;
import android.text.format.Time;

/**
 * Created by wds on 17.12.2014.
 */
public class ApplicationData extends Application {
    TimeSpan startStopPeriod;
    Time timeInSleep;

    {
        startStopPeriod = new TimeSpan();
        timeInSleep = new Time();
    }

    public TimeSpan getStartStopPeriod() {
        return startStopPeriod;
    }

    public void setStartStopPeriod(TimeSpan startStopPeriod) {
        this.startStopPeriod = startStopPeriod;
    }

    public Time getTimeInSleep() {
        return timeInSleep;
    }

    public void setTimeInSleep(Time timeInSleep) {
        this.timeInSleep = timeInSleep;
    }
}