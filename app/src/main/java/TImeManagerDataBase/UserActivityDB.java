package TImeManagerDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data base manipulation class
 */
public class UserActivityDB extends SQLiteOpenHelper {
    /**
     * name of the database
     */
    private final static String DATABASE_NAME = "timerAppDB";

    /**
     * Constructor
     * @param context
     */
    public UserActivityDB(Context context) {
        super(context, DATABASE_NAME, null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(userActivitiesTable.createTable());
        db.execSQL(timePeriodTable.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(userActivitiesTable.DropIfExistst());
        db.execSQL(timePeriodTable.DropIfExistst());
        onCreate(db);
    }

    /**
     * add user's activity to the database. The object passed as an argument will have
     * it's id set by this function.
     * @param activity activity to add
     */
    public void addUserActivity(UserActivityDBTableEntry activity) {
        ContentValues cv = new ContentValues();
        cv.put(userActivitiesTable.NAME_FIELD, activity.getName());

        SQLiteDatabase db = getWritableDatabase();
        long idActivity = db.insert(userActivitiesTable.TABLE_NAME, null, cv);
        activity.setId(idActivity);
    }

    /**
     * Get all user's activities
     * @return a list filled with all user activities in the database
     */
    public List<UserActivityDBTableEntry> getAllUserActivities() {
        List<UserActivityDBTableEntry> activitiesFromDB = new ArrayList<UserActivityDBTableEntry>();

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + userActivitiesTable.TABLE_NAME, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            activitiesFromDB.add(new UserActivityDBTableEntry(name, id));
            cursor.moveToNext();
        }

        return activitiesFromDB;
    }

    /**
     * Remove user's activity passed as an argument from the database
     * @param ua
     */
    public void removeUserActivity(UserActivityDBTableEntry ua) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(userActivitiesTable.TABLE_NAME, userActivitiesTable.ID_FIELD + "= '" + ua.getId() + "'", null);
    }

    /**
     * Get all time periods in the database
     * @return a list filled with all time periods from the database
     */
    public List<TimePeriodDBTableEntry> getAllTimePeriods() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(timePeriodTable.getAll(), null);

        cursor.moveToFirst();

        return getTimePeriodListFromCursor(cursor);
    }

    /**
     * Get time periods that has the provided as arguments session number and user activity id
     * @param session session number of the needed time periods
     * @param userActivityID user activity id of the needed time periods
     * @returna list filled with time periods that has the needed session number and user activity id
     */
    public List<TimePeriodDBTableEntry> getAllTimePeriods(long session, long userActivityID) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(timePeriodTable.getAll() +
                " WHERE " + timePeriodTable.SESSION_NUMBER + " = " + session +
                " AND " + timePeriodTable.ID_USER_ACTIVITY + " = " + userActivityID, null);

        cursor.moveToFirst();

        return getTimePeriodListFromCursor(cursor);
    }

    /**
     * Returns a list filled with time period entries taken with cursor passed as a parameter
     * @param cursor cursor with table entries
     * @return list with time period entries
     */
    private List<TimePeriodDBTableEntry> getTimePeriodListFromCursor(Cursor cursor) {
        List<TimePeriodDBTableEntry> times = new ArrayList<TimePeriodDBTableEntry>();

        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(cursor.getColumnIndex(timePeriodTable.ID_FIELD));
            long idUserActivity = cursor.getLong(cursor.getColumnIndex(timePeriodTable.ID_USER_ACTIVITY));
            int secsPassed = Integer.valueOf(cursor.getString(cursor.getColumnIndex(timePeriodTable.TIME_PASSED_FIELD)));
            long sessionNumber = cursor.getLong(cursor.getColumnIndex(timePeriodTable.SESSION_NUMBER));
            Date startedAt = null;

            try {
                startedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(cursor.getString(
                        cursor.getColumnIndex(timePeriodTable.DATE_STARTED_FIELD)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            times.add(new TimePeriodDBTableEntry(id, idUserActivity, startedAt, secsPassed, sessionNumber));
            cursor.moveToNext();
        }

        return times;
    }

    /**
     * add new time period entry to the database
     * @param timePeriod timePeriod to add
     */
    public void addNewTimePeriod(TimePeriodDBTableEntry timePeriod) {
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(timePeriodTable.TABLE_NAME, null, timePeriodTable.addTimePassedRecord(timePeriod));
        timePeriod.setId(id);
    }

    /**
     * Get last entry's session number
     * @return session number
     */
    public long getLastSessionNumber() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(timePeriodTable.getAll(), null);
        cursor.moveToLast();
        long lastSessionNumber = 0;

        try {
            cursor.getLong(cursor.getColumnIndex(timePeriodTable.SESSION_NUMBER));
        } catch (CursorIndexOutOfBoundsException e) {
            Log.w(timePeriodTable.SESSION_NUMBER, e.getMessage());
        }

        return lastSessionNumber;
    }
}
