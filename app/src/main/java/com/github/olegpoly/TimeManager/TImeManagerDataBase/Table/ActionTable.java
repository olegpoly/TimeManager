package com.github.olegpoly.TimeManager.TImeManagerDataBase.Table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.olegpoly.TimeManager.TImeManagerDataBase.DataBase;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.ActionDBEntry;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the activities table in database
 */
public class ActionTable {
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
    static public void add(ActionDBEntry activity) {
        ContentValues cv = new ContentValues();
        cv.put(ActionTable.NAME_FIELD, activity.getActivityName());

        DataBase database = DataBase.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();
        long idActivity = db.insert(ActionTable.TABLE_NAME, null, cv);
        activity.setId(idActivity);
    }

    /**
     * Get all user's activities
     * @return a list filled with all user activities in the database
     */
    static public List<ActionDBEntry> getAll() {
        List<ActionDBEntry> activitiesFromDB = new ArrayList<>();

        DataBase database = DataBase.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + ActionTable.TABLE_NAME, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            activitiesFromDB.add(new ActionDBEntry(name, id));
            cursor.moveToNext();
        }

        return activitiesFromDB;
    }

    /**
     * Remove user's activity passed as an argument from the database
     * @param ua user activity to delete
     */
    static public void remove(ActionDBEntry ua) {
        DataBase database = DataBase.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();
        db.delete(ActionTable.TABLE_NAME, ActionTable.ID_FIELD + "= '" + ua.getId() + "'", null);
    }

    /**
     * Find UserActivityDBTableEntry from database by id.
     * @param id needed UserActivityDBTableEntry's id
     * @return UserActivityDBTableEntry
     * @see com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.ActionDBEntry
     * @throws SQLDataException if UserActivityDBTableEntry is not found
     */
    static public ActionDBEntry get(long id) throws SQLDataException {
        DataBase database = DataBase.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ActionTable.TABLE_NAME + " where " +
                ActionTable.ID_FIELD + " = " + id, null);

        // if no rows found throw an exception
        if (cursor.getCount() == 0) {
            throw new SQLDataException();
        }

        // create and initialize instance
        cursor.moveToFirst();
        String activityName = cursor.getString(cursor.getColumnIndex(ActionTable.NAME_FIELD));
        long activityId = cursor.getLong(cursor.getColumnIndex(ActionTable.ID_FIELD));
        ActionDBEntry userActivity = new ActionDBEntry(activityName, activityId);

        return userActivity;
    }

    /**
     * Check if activity with the name passed as the parameter exists in the database
     * @param activityName name of the activity
     * @return true if exists, false otherwise
     */
    static public boolean checkIfExists(String activityName) {
        DataBase database = DataBase.getInstance();
        SQLiteDatabase db = database.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + ActionTable.TABLE_NAME +
                " WHERE " + ActionTable.NAME_FIELD + " = '" + activityName + "'", null);

        if (c.getCount() == 0)
            return false;

        return true;
    }
}