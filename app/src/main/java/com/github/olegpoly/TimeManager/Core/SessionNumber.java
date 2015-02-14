package com.github.olegpoly.TimeManager.Core;

import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.TimePeriodTable;

/**
 * Singleton.
 * Represents the current session number. All time periods in database is associated with a
 * session number. To start a new timer - start a new session by using a value that hasn't been
 * used yet.
 */
public class SessionNumber {
    /**
     * singleton's instance
     */
    private static SessionNumber instance = new SessionNumber();
    /**
     * session number descriptor
     */
    private Long currentSessionNumber;

    /**
     * Constructor
     */
    private SessionNumber() {
        currentSessionNumber = TimePeriodTable.getLastSessionNumber();
    }

    /**
     * get singleton's instance
     *
     * @return instance of the session number
     */
    public static SessionNumber getInstance() {
        return instance;
    }

    /**
     * Get current session number
     *
     * @return current session number
     */
    public Long getCurrentSessionNumber() {
        return currentSessionNumber;
    }

    /**
     * Set current session number
     *
     * @param currentSessionNumber
     */
    public void setCurrentSessionNumber(Long currentSessionNumber) {
        this.currentSessionNumber = currentSessionNumber;
    }

    /**
     * Start a new session.
     */
    public void startNewSession() {
        currentSessionNumber++;
    }
}
