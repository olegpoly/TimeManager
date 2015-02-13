package com.example.qwerty.timemanager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Represents a time interval between two points.
 */
public class TimeSpan {
    Calendar startDate;
    Calendar endDate;

    /**
     * Get the start date
     *
     * @return the start date
     */
    public Calendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date
     *
     * @param startDate the start date
     */
    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    /**
     * Get the end date
     *
     * @return the end date
     */
    public Calendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date
     *
     * @param endDate the end date
     */
    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    /**
     * @param timeUnit type of the return value
     * @return time period between start date and end date
     */
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

    /**
     * Reset the current object. Both start and end dates are set to null.
     */
    public void reset() {
        startDate = null;
        endDate = null;
    }

    /**
     * Checks if this object is reseted or not.
     *
     * @return true if reseted, false otherwise
     */
    public boolean isReseted() {
        if (startDate == null && endDate == null)
            return true;

        return false;
    }
}
