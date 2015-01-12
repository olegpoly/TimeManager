package TImeManagerDataBase;

import android.content.ContentValues;
import java.text.SimpleDateFormat;

/**
 * Represents the time_period table in database
 */
class timePeriodTable {
    static final String TABLE_NAME = "time_period";
    static final String ID_FIELD = "id";
    static final String DATE_STARTED_FIELD = "date_started";
    static final String TIME_PASSED_FIELD = "secs_passed";
    static final String ID_USER_ACTIVITY = "id_user_activity";
    static final String SESSION_NUMBER = "session_number";

    /**
     * Get sql command for creating the table
     * @return sql command in string
     */
    static public String createTable() {
        return "create table " + TABLE_NAME + "(" +
                ID_FIELD + " integer primary key, " + DATE_STARTED_FIELD + " datetime, " + TIME_PASSED_FIELD + " int, " +
                ID_USER_ACTIVITY + " int not null, " + SESSION_NUMBER + " integer)";
    }

    /**
     * Get sql command for dropping the table if it exists
     * @return sql command in string
     */
    static public String DropIfExists() {
        return "drop table if exists " + TABLE_NAME;
    }

    static public ContentValues addTimePassedRecord(TimePeriodDBTableEntry tp) {
        ContentValues cv = new ContentValues();
        cv.put(DATE_STARTED_FIELD, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(tp.getDateStarted()));
        cv.put(TIME_PASSED_FIELD, tp.getSecsPassed());
        cv.put(ID_USER_ACTIVITY, tp.getIdUserActivity());
        cv.put(SESSION_NUMBER, tp.getSessionNumber());
        return cv;
    }

    /**
     * Get sql command for getting all table's entries
     * @return sql command in string
     */
    static public String getAllEntries() {
        String sql = "select * from " + TABLE_NAME;

        return sql;
    }

}