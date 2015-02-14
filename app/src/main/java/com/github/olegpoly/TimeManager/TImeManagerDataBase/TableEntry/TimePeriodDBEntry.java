package com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry;

import java.util.Date;

/**
 * Data base table that represents time period
 */
public class TimePeriodDBEntry {
    /**
     * table entry id
     */
    private long id;
    /**
     * start of time period
     */
    private Date dateStarted;
    /**
     * seconds passed since dateStarted
     */
    private long secsPassed;
    /**
     * UserActivity table entry associated with this entry
     */
    private long idUserActivity;
    /**
     * session number associated with this entry
     */
    private long sessionNumber;

    /**
     * Constructor
     * @param started start of time period
     * @param secsPassed seconds passed since dateStarted
     */
    public TimePeriodDBEntry(Date started, long secsPassed) {
        this.dateStarted = started;
        this.secsPassed = secsPassed;
    }

    /**
     * Constructor
     * @param idUserActivity UserActivity table entry associated with this entry
     * @param started start of time period
     * @param secsPassed seconds passed since dateStarted
     * @param sessionNumber session number associated with this entry
     */
    public TimePeriodDBEntry(long idUserActivity, Date started, long secsPassed, long sessionNumber) {
        this.setDateStarted(started);
        this.setSecsPassed(secsPassed);
        this.setIdUserActivity(idUserActivity);
        this.setSessionNumber(sessionNumber);
    }

    /**
     * Constructor
     * @param id table entry id
     * @param idUserActivity UserActivity table entry associated with this entry
     * @param started start of time period
     * @param secsPassed seconds passed since dateStarted
     * @param sessionNumber session number associated with this entry
     */
    public TimePeriodDBEntry(long id, long idUserActivity, Date started, long secsPassed, long sessionNumber) {
        this.setDateStarted(started);
        this.setSecsPassed(secsPassed);
        this.setId(id);
        this.setIdUserActivity(idUserActivity);
        this.setSessionNumber(sessionNumber);
    }

    /**
     * Get session number
     * @return session number
     */
    public long getSessionNumber() {
        return sessionNumber;
    }

    /**
     * Set session number
     * @param sessionNumber session number
     */
    public void setSessionNumber(long sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    /**
     * Get id
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Set id
     * @param id id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get user activity
     * @return activity
     */
    public long getIdUserActivity() {
        return idUserActivity;
    }

    /**
     * Set user's activity id
     * @param idUserActivity date started
     */
    public void setIdUserActivity(long idUserActivity) {
        this.idUserActivity = idUserActivity;
    }

    @Override
    public String toString() {
        return dateStarted.toString() + " " + String.valueOf(secsPassed);
    }

    /**
     * Get date started
     * @return date started
     */
    public Date getDateStarted() {
        return dateStarted;
    }

    /**
     * Set date started
     * @param dateStarted date started
     */
    public void setDateStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    /**
     * Get seconds passed since date started
     * @return amount of seconds passed since date started
     */
    public long getSecsPassed() {
        return secsPassed;
    }

    /**
     * Set amount of seconds passed since date started
     * @param secsPassed amount of seconds passed since date started
     */
    public void setSecsPassed(long secsPassed) {
        this.secsPassed = secsPassed;
    }
}
