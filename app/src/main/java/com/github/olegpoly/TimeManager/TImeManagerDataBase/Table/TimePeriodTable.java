package com.github.olegpoly.TimeManager.TImeManagerDataBase.Table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.TimePeriodDBEntry;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.UserActivityDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents the time_period table in database
 */
public class TimePeriodTable {
    public static final String TABLE_NAME = "time_period";
    public static final String ID_FIELD = "id";
    public static final String DATE_STARTED_FIELD = "date_started";
    public static final String TIME_PASSED_FIELD = "secs_passed";
    public static final String SESSION_NUMBER = "session_number";
    public static final String ID_USER_ACTIVITY = "id_user_activity";

    /**
     * Get sql command for creating the table
     * @return sql command in string
     */
    static public String createTable() {
        return "create table " + TABLE_NAME + "(" +
                ID_FIELD + " integer primary key, " + DATE_STARTED_FIELD + " datetime, " + TIME_PASSED_FIELD + " int, " +
                ID_USER_ACTIVITY + " int not null, " + SESSION_NUMBER + " integer, " +
                " FOREIGN KEY (" + ID_USER_ACTIVITY + ") REFERENCES " +
                UserActivitiesTable.TABLE_NAME + " (" + UserActivitiesTable.ID_FIELD + "))";
    }

    /**
     * Get sql command for dropping the table if it exists
     * @return sql command in string
     */
    static public String DropIfExists() {
        return "drop table if exists " + TABLE_NAME;
    }

    static public ContentValues makeContentValues(TimePeriodDBEntry tp) {
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

    /**
     * Get all time periods in the database
     * @return a list filled with all time periods from the database
     */
    static public List<TimePeriodDBEntry> getAll() {
        UserActivityDB database = UserActivityDB.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = db.rawQuery(TimePeriodTable.getAllEntries(), null);

        cursor.moveToFirst();

        return getTimePeriodListFromCursor(cursor);
    }

    /**
     * Get time periods that has the provided as arguments session number and user activity id
     * @param session session number of the needed time periods
     * @param userActivityID user activity id of the needed time periods
     * @return a list filled with time periods that has the needed session number and user activity id
     */
    static public List<TimePeriodDBEntry> getAll(long session, long userActivityID) {
        UserActivityDB database = UserActivityDB.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = db.rawQuery(TimePeriodTable.getAllEntries() +
                " WHERE " + TimePeriodTable.SESSION_NUMBER + " = " + session +
                " AND " + TimePeriodTable.ID_USER_ACTIVITY + " = " + userActivityID, null);

        cursor.moveToFirst();

        return getTimePeriodListFromCursor(cursor);
    }

    // TODO: write comment
    static public List<TimePeriodDBEntry> getAll(long userActivityID) {
        UserActivityDB database = UserActivityDB.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = db.rawQuery(TimePeriodTable.getAllEntries() +
                " WHERE " + TimePeriodTable.ID_USER_ACTIVITY + " = " + userActivityID, null);

        cursor.moveToFirst();

        return getTimePeriodListFromCursor(cursor);
    }

    // TODO: write comment
    static public Long getAllTime(long userActivityID) {
        UserActivityDB database = UserActivityDB.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = db.rawQuery(TimePeriodTable.getAllEntries() +
                " WHERE " + TimePeriodTable.ID_USER_ACTIVITY + " = " + userActivityID, null);

        cursor.moveToFirst();

        List<TimePeriodDBEntry> all = getTimePeriodListFromCursor(cursor);

        Long result = 0L;

        for (TimePeriodDBEntry tp : all) {
            result += tp.getSecsPassed();
        }

        return result;
    }

    // TODO: write comment
    static public Long getAllTime2(long userActivityID) {
        UserActivityDB database = UserActivityDB.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();

        String sql = "SELECT sum(strftime('%s', total_expend_time) - strftime('%s', '00:00:00')) FROM "
                + TimePeriodTable.TABLE_NAME + " WHERE " + TimePeriodTable.ID_USER_ACTIVITY + " =  " + userActivityID;

        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();

        Long result = cursor.getLong(cursor.getColumnIndex("SUM"));

        return result;
    }

    /**
     * Returns a list filled with time period entries taken with cursor passed as a parameter
     * The cursor must be setup by the caller.
     * This function does not move cursor to any position, it will start from the current cursor's position.
     * @param cursor cursor with table entries
     * @return list with time period entries
     */
    private static List<TimePeriodDBEntry> getTimePeriodListFromCursor(Cursor cursor) {
        List<TimePeriodDBEntry> times = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(cursor.getColumnIndex(TimePeriodTable.ID_FIELD));
            long idUserActivity = cursor.getLong(cursor.getColumnIndex(TimePeriodTable.ID_USER_ACTIVITY));
            int secsPassed = Integer.valueOf(cursor.getString(cursor.getColumnIndex(TimePeriodTable.TIME_PASSED_FIELD)));
            long sessionNumber = cursor.getLong(cursor.getColumnIndex(TimePeriodTable.SESSION_NUMBER));
            Date startedAt = null;

            try {
                startedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(cursor.getString(
                        cursor.getColumnIndex(TimePeriodTable.DATE_STARTED_FIELD)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            times.add(new TimePeriodDBEntry(id, idUserActivity, startedAt, secsPassed, sessionNumber));
            cursor.moveToNext();
        }

        return times;
    }

    /**
     * add new time period entry to the database
     * @param timePeriod timePeriod to add
     */
    static public void add(TimePeriodDBEntry timePeriod) {
        UserActivityDB database = UserActivityDB.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();
        long id = db.insert(TimePeriodTable.TABLE_NAME, null, makeContentValues(timePeriod));
        timePeriod.setId(id);
    }

    /**
     * Get last entry's session number
     * @return session number
     */
    static public long getLastSessionNumber() {
        UserActivityDB database = UserActivityDB.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = db.rawQuery(TimePeriodTable.getAllEntries(), null);

        cursor.moveToLast();
        long lastSessionNumber = 0;

        try {
            lastSessionNumber = cursor.getLong(cursor.getColumnIndex(TimePeriodTable.SESSION_NUMBER));
        } catch (CursorIndexOutOfBoundsException e) {
            Log.w(TimePeriodTable.SESSION_NUMBER, e.getMessage());
        }

        return lastSessionNumber;
    }
}
