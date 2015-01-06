package com.example.qwerty.timemanager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Represents a time interval.
 */
public class TimeSpan {
    Calendar startDate;
    Calendar endDate;

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public long getDuration(TimeUnit timeUnit) {
        long returnValue = 0;

        switch (timeUnit) {
            case MILLISECONDS:
                returnValue = endDate.getTimeInMillis() - startDate.getTimeInMillis();
                break;
            case SECONDS:
                returnValue = TimeUnit.MILLISECONDS.toSeconds(endDate.getTimeInMillis()) -
                        TimeUnit.MILLISECONDS.toSeconds(startDate.getTimeInMillis());
                break;
            case MINUTES:
                returnValue = TimeUnit.MILLISECONDS.toMinutes(endDate.getTimeInMillis()) -
                        TimeUnit.MILLISECONDS.toMinutes(startDate.getTimeInMillis());
                break;
        }

        return returnValue;
    }

    public void reset() {
        startDate = null;
        endDate = null;
    }

    public boolean isReseted() {
        if (startDate == null && endDate == null)
            return true;

        return false;
    }
}
