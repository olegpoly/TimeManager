package TImeManagerDataBase;

/**
 * Represents the activities table in database
 */
class userActivitiesTable {
    static final String TABLE_NAME = "activities";
    static final String ID_FIELD = "id";
    static final String NAME_FIELD = "name";

    /**
     * Get sql command for creating the table
     * @return sql command in string
     */
    static public String createTable() {
        return "create table " + TABLE_NAME + "(" + ID_FIELD + " integer primary key, " + NAME_FIELD + " text)";
    }

    /**
     * Get sql command for dropping the table if it exists
     * @return sql command in string
     */
    static public String DropIfExists() {
        return "drop table if exists " + TABLE_NAME;
    }
}
