package TImeManagerDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserActivityDB extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "timerAppDB";

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

    // add user's activity and set it's new id
    public void addUserActivity(UserActivityDBTableEntry activity) {
        ContentValues cv = new ContentValues();
        cv.put(userActivitiesTable.NAME_FIELD, activity.getName());

        SQLiteDatabase db = getWritableDatabase();
        long idActivity = db.insert(userActivitiesTable.TABLE_NAME, null, cv);
        activity.setId(idActivity);
    }

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

    public void removeUserActivity(UserActivityDBTableEntry ua) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(userActivitiesTable.TABLE_NAME, userActivitiesTable.ID_FIELD + "= '" + ua.getId() + "'", null);
    }

    public List<TimePeriodDBTableEntry> getAllPassedTimes() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(timePeriodTable.getAll(), null);

        cursor.moveToFirst();

        return getTimePeriodListFromCursor(cursor);
    }

    public List<TimePeriodDBTableEntry> getAllPassedTimes(long session, long userActivityID) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(timePeriodTable.getAll() +
                " WHERE " + timePeriodTable.SESSION_NUMBER + " = " + session +
                " AND " + timePeriodTable.ID_USER_ACTIVITY + " = " + userActivityID, null);

        cursor.moveToFirst();

        return getTimePeriodListFromCursor(cursor);
    }

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

    public void putNewTime(TimePeriodDBTableEntry tp) {
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(timePeriodTable.TABLE_NAME, null, timePeriodTable.addTimePassedRecord(tp));
        tp.setId(id);
    }

    public long getLastSessionNumber() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(timePeriodTable.getAll(), null);
        cursor.moveToLast();
        long lastSessionNumber = 0;

        //try {
            cursor.getLong(cursor.getColumnIndex(timePeriodTable.SESSION_NUMBER));
       /* } catch (CursorIndexOutOfBoundsException e) {
            Log.w(timePeriodTable.SESSION_NUMBER, e.getMessage());
        }*/

        return lastSessionNumber;
    }

    static class userActivitiesTable {
        static private final String TABLE_NAME = "activities";
        static private final String ID_FIELD = "id";
        static private final String NAME_FIELD = "name";

        static public String createTable() {
            return "create table " + TABLE_NAME + "(" + ID_FIELD + " integer primary key, " + NAME_FIELD + " text)";
        }

        static public String DropIfExistst() {
            return "drop table if exists " + TABLE_NAME;
        }
    }

    static class timePeriodTable {
        static private final String TABLE_NAME = "time_period";
        static private final String ID_FIELD = "id";
        static private final String DATE_STARTED_FIELD = "date_started";
        static private final String TIME_PASSED_FIELD = "secs_passed";
        static private final String ID_USER_ACTIVITY = "id_user_activity";
        static private final String SESSION_NUMBER = "session_number";

        static public String createTable() {
            return "create table " + TABLE_NAME + "(" +
                    ID_FIELD + " integer primary key, " + DATE_STARTED_FIELD + " datetime, " + TIME_PASSED_FIELD + " int, " +
                    ID_USER_ACTIVITY + " int not null, " + SESSION_NUMBER + " integer)";
        }

        static public String DropIfExistst() {
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

        static public String getAll() {
            String sql = "select * from " + TABLE_NAME;

            return sql;
        }

    }
}
