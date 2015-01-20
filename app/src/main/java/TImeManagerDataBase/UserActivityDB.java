package TImeManagerDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.qwerty.timemanager.ApplicationData;

import java.sql.SQLDataException;
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
     * singleton instance
     */
    private static UserActivityDB database;

    /**
     * Get instance of the database
     * @return instance of the database
     */
    public static UserActivityDB getInstance() {
        if (database == null) {
            // guarantee that only one database helper will exist across the entire application's lifecycle.
            database = new UserActivityDB(ApplicationData.getAppContext());
        }

        return database;
    }

    /**
     * Constructor
     * @param context context of the database
     */
    private UserActivityDB(Context context) {
        super(context, DATABASE_NAME, null, 10);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table
        db.execSQL(UserActivitiesTable.createTable());
        db.execSQL(TimePeriodTable.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete tables
        db.execSQL(UserActivitiesTable.DropIfExists());
        db.execSQL(TimePeriodTable.DropIfExists());

        // recreate tables
        onCreate(db);
    }

    /**
     * add user's activity to the database. The object passed as an argument will have
     * it's id set by this function.
     * @param activity activity to add
     */
    public void addUserActivity(UserActivityDBTableEntry activity) {
        ContentValues cv = new ContentValues();
        cv.put(UserActivitiesTable.NAME_FIELD, activity.getName());

        SQLiteDatabase db = getWritableDatabase();
        long idActivity = db.insert(UserActivitiesTable.TABLE_NAME, null, cv);
        activity.setId(idActivity);
    }

    /**
     * Get all user's activities
     * @return a list filled with all user activities in the database
     */
    public List<UserActivityDBTableEntry> getAllUserActivities() {
        List<UserActivityDBTableEntry> activitiesFromDB = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + UserActivitiesTable.TABLE_NAME, null);

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
     * @param ua user activity to delete
     */
    public void removeUserActivity(UserActivityDBTableEntry ua) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(UserActivitiesTable.TABLE_NAME, UserActivitiesTable.ID_FIELD + "= '" + ua.getId() + "'", null);
    }

    /**
     * Get all time periods in the database
     * @return a list filled with all time periods from the database
     */
    public List<TimePeriodDBTableEntry> getAllTimePeriods() {
        SQLiteDatabase db = getWritableDatabase();
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
    public List<TimePeriodDBTableEntry> getAllTimePeriods(long session, long userActivityID) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(TimePeriodTable.getAllEntries() +
                " WHERE " + TimePeriodTable.SESSION_NUMBER + " = " + session +
                " AND " + TimePeriodTable.ID_USER_ACTIVITY + " = " + userActivityID, null);

        cursor.moveToFirst();

        return getTimePeriodListFromCursor(cursor);
    }

    /**
     * Returns a list filled with time period entries taken with cursor passed as a parameter
     * The cursor must be setup by the caller.
     * This function does not move cursor to any position, it will start from the current cursor's position.
     * @param cursor cursor with table entries
     * @return list with time period entries
     */
    private List<TimePeriodDBTableEntry> getTimePeriodListFromCursor(Cursor cursor) {
        List<TimePeriodDBTableEntry> times = new ArrayList<>();

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
        long id = db.insert(TimePeriodTable.TABLE_NAME, null, TimePeriodTable.addTimePassedRecord(timePeriod));
        timePeriod.setId(id);
    }

    /**
     * Get last entry's session number
     * @return session number
     */
    public long getLastSessionNumber() {
        SQLiteDatabase db = getWritableDatabase();
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

    /**
     * Find UserActivityDBTableEntry from database by id.
     * @param id needed UserActivityDBTableEntry's id
     * @return UserActivityDBTableEntry
     * @see UserActivityDBTableEntry
     * @throws SQLDataException if UserActivityDBTableEntry is not found
     */
    public UserActivityDBTableEntry getActivityById(long id) throws SQLDataException {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + UserActivitiesTable.TABLE_NAME + " where " +
        UserActivitiesTable.ID_FIELD + " = " + id, null);

        // if no rows found throw an exception
        if (cursor.getCount() == 0) {
            throw new SQLDataException();
        }

        // create and initialize instance
        cursor.moveToFirst();
        String activityName = cursor.getString(cursor.getColumnIndex(UserActivitiesTable.NAME_FIELD));
        long activityId = cursor.getLong(cursor.getColumnIndex(UserActivitiesTable.ID_FIELD));
        UserActivityDBTableEntry userActivity = new UserActivityDBTableEntry(activityName, activityId);

        return userActivity;
    }

    /**
     * Check if activity with the name passed as the parameter exists in the database
     * @param activityName name of the activity
     * @return true if exists, false otherwise
     */
    public boolean checkIfActivityExists(String activityName) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + UserActivitiesTable.TABLE_NAME +
                " WHERE " + UserActivitiesTable.NAME_FIELD + " = '" + activityName + "'", null);

        if (c.getCount() == 0)
            return false;

        return true;
    }
}
