package com.example.qwerty.timemanager;

import java.util.Date;

/**
 * Created by Oleg on 11/23/2014.
 */
public class TimePeriodDBTableEntry {
    private long id;
    private Date dateStarted;
    private long secsPassed;
    private long idUserActivity;
    private long sessionNumber;

    public TimePeriodDBTableEntry(Date started, long secsPassed) {
        this.dateStarted = started;
        this.secsPassed = secsPassed;
    }

    public TimePeriodDBTableEntry(long id, long idUserActivity, Date started, long secsPassed, long sessionNumber) {
        this.dateStarted = started;
        this.secsPassed = secsPassed;
        this.setId(id);
        this.setIdUserActivity(idUserActivity);
        this.setSessionNumber(sessionNumber);
    }

    public long getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(long sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdUserActivity() {
        return idUserActivity;
    }

    public void setIdUserActivity(long idUserActivity) {
        this.idUserActivity = idUserActivity;
    }

    @Override
    public String toString() {
        return dateStarted.toString() + " " + String.valueOf(secsPassed);
    }

    public Date getStarted() {
        return dateStarted;
    }

    public void setStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public long getSecsPassed() {
        return secsPassed;
    }

    public void setSecsPassed(int secsPassed) {
        this.secsPassed = secsPassed;
    }
}
