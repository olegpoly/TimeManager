package TImeManagerDataBase.Table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

import TImeManagerDataBase.TableEntry.UserActivityDBTableEntry;
import TImeManagerDataBase.UserActivityDB;

/**
 * Represents the activities table in database
 */
public class UserActivitiesTable {
    public static final String TABLE_NAME = "activities";
    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";

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


    /**
     * add user's activity to the database. The object passed as an argument will have
     * it's id set by this function.
     * @param activity activity to add
     */
    static public void add(UserActivityDBTableEntry activity) {
        ContentValues cv = new ContentValues();
        cv.put(UserActivitiesTable.NAME_FIELD, activity.getActivityName());

        UserActivityDB database = UserActivityDB.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();
        long idActivity = db.insert(UserActivitiesTable.TABLE_NAME, null, cv);
        activity.setId(idActivity);
    }

    /**
     * Get all user's activities
     * @return a list filled with all user activities in the database
     */
    static public List<UserActivityDBTableEntry> getAll() {
        List<UserActivityDBTableEntry> activitiesFromDB = new ArrayList<>();

        UserActivityDB database = UserActivityDB.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();

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
    static public void remove(UserActivityDBTableEntry ua) {
        UserActivityDB database = UserActivityDB.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();
        db.delete(UserActivitiesTable.TABLE_NAME, UserActivitiesTable.ID_FIELD + "= '" + ua.getId() + "'", null);
    }

    /**
     * Find UserActivityDBTableEntry from database by id.
     * @param id needed UserActivityDBTableEntry's id
     * @return UserActivityDBTableEntry
     * @see UserActivityDBTableEntry
     * @throws SQLDataException if UserActivityDBTableEntry is not found
     */
    static public UserActivityDBTableEntry get(long id) throws SQLDataException {
        UserActivityDB database = UserActivityDB.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();

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
    static public boolean checkIfExists(String activityName) {
        UserActivityDB database = UserActivityDB.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + UserActivitiesTable.TABLE_NAME +
                " WHERE " + UserActivitiesTable.NAME_FIELD + " = '" + activityName + "'", null);

        if (c.getCount() == 0)
            return false;

        return true;
    }
}