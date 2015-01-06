package com.example.qwerty.timemanager;

/**
 * Created by Oleg on 11/21/2014.
 */
public class UserActivityDBTableEntry {
    private long id;
    private String activityName;

    public UserActivityDBTableEntry(String activityName) {
        this.setName(activityName);
    }

    public UserActivityDBTableEntry(String name, long id) {
        this.setName(name);
        this.setId(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return activityName;
    }

    public void setName(String activityName) {
        this.activityName = activityName;
    }

    @Override
    public String toString() {
        return activityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserActivityDBTableEntry that = (UserActivityDBTableEntry) o;

        if (id != that.id) return false;
        if (activityName != null ? !activityName.equals(that.activityName) : that.activityName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (activityName != null ? activityName.hashCode() : 0);
        return result;
    }
}
