package com.example.qwerty.timemanager;

import TImeManagerDataBase.UserActivityDB;

/**
 * Singleton.
 * Represents the current session number. All time periods in database is associated with a
 * session number. To start a new timer - start a new session by using a value that hasn't been
 * used yet.
 */
public class SessionNumber {
    /**
     * session number descriptor
     */
    private Long currentSessionNumber;

    /**
     * Constructor
     */
    private SessionNumber() {
        UserActivityDB db = UserActivityDB.getInstance(ApplicationData.getAppContext());
        currentSessionNumber = db.getLastSessionNumber();
    }

    /**
     * Get current session number
     * @return current session number
     */
    public Long getCurrentSessionNumber() {
        return currentSessionNumber;
    }

    /**
     * Set current session number
     * @param currentSessionNumber
     */
    public void setCurrentSessionNumber(Long currentSessionNumber) {
        this.currentSessionNumber = currentSessionNumber;
    }

    /**
     * singleton's instance
     */
    private static SessionNumber instance = new SessionNumber();

    /**
     * get singleton's instance
     * @return instance of the session number
     */
    public static SessionNumber getInstance() {
        return instance;
    }

    /**
     * Start a new session.
     */
    public void startNewSession() {
        currentSessionNumber++;
    }
}
