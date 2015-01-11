package TImeManagerDataBase;

/**
 * Represents the activities table in database
 */
class userActivitiesTable {
    static final String TABLE_NAME = "activities";
    static final String ID_FIELD = "id";
    static final String NAME_FIELD = "name";

    static public String createTable() {
        return "create table " + TABLE_NAME + "(" + ID_FIELD + " integer primary key, " + NAME_FIELD + " text)";
    }

    static public String DropIfExistst() {
        return "drop table if exists " + TABLE_NAME;
    }
}
