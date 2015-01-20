package TImeManagerDataBase;

/**
 * Data base table that represents user's activity
 */
public class UserActivityDBTableEntry {
    /**
     * table entry's id
     */
    private long id;
    /**
     * name of the activity
     */
    private String activityName;

    /**
     * Constructor
     * @param activityName name of the activity
     */
    public UserActivityDBTableEntry(String activityName) {
        this.setName(activityName);
    }

    /**
     * Constructor
     * @param name name of the activity
     * @param id table entry's id
     */
    public UserActivityDBTableEntry(String name, long id) {
        this.setName(name);
        this.setId(id);
    }

    /**
     * Get table entry's id
     * @return table entry's id
     */
    public long getId() {
        return id;
    }

    /**
     * Set table entry's id
     * @param id table entry's id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get name of the activity
     * @return name of the activity
     */
    public String getName() {
        return activityName;
    }

    /**
     * Set name of the activity
     */
    public void setName(String activityName) {
        this.activityName = activityName;
    }

    @Override
    public String toString() {
        return activityName;
    }

    /**
     * Implementation of the equals method
     * @param o check equality to this object
     * @return true if equals, false otherwise
     */
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

    /**
     * Calculation of hash code
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (activityName != null ? activityName.hashCode() : 0);
        return result;
    }
}
