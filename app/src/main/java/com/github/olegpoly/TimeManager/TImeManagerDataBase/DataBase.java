package com.github.olegpoly.TimeManager.TImeManagerDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.olegpoly.TimeManager.Core.ApplicationData;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.TimePeriodTable;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.ActionTable;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.TimePeriodDBEntry;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.ActionDBEntry;

import java.util.Date;

/**
 * Data base manipulation class
 */
public class DataBase extends SQLiteOpenHelper {
    /**
     * name of the database
     */
    private final static String DATABASE_NAME = "timerAppDB";

    /**
     * singleton instance
     */
    private static DataBase database;

    /**
     * Get instance of the database
     * @return instance of the database
     */
    public static DataBase getInstance() {
        if (database == null) {
            // guarantee that only one database helper will exist across the entire application's lifecycle.
            database = new DataBase(ApplicationData.getAppContext());
        }

        return database;
    }

    /**
     * Constructor
     * @param context context of the database
     */
    private DataBase(Context context) {
        super(context, DATABASE_NAME, null, 10);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table
        db.execSQL(ActionTable.createTable());
        db.execSQL(TimePeriodTable.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete tables
        db.execSQL(ActionTable.DropIfExists());
        db.execSQL(TimePeriodTable.DropIfExists());

        // recreate tables
        onCreate(db);
    }

    static public void deleteAll() {
        TimePeriodTable.DropIfExists();
        ActionTable.DropIfExists();

        // recreate tables
        ActionTable.createTable();
        TimePeriodTable.createTable();
    }

    static public void addAll() {
        ActionDBEntry ua1 = new ActionDBEntry("ac1");
        ActionTable.add(ua1);
        ActionDBEntry ua2 = new ActionDBEntry("ac2");
        ActionTable.add(ua2);

        TimePeriodDBEntry tp1 = new TimePeriodDBEntry(ua1.getId(), new Date(), 5, 0);
        TimePeriodTable.add(tp1);

        tp1 = new TimePeriodDBEntry(ua2.getId(), new Date(), 5, 0);
        TimePeriodTable.add(tp1);

        tp1 = new TimePeriodDBEntry(ua1.getId(), new Date(), 5, 1);
        TimePeriodTable.add(tp1);

        tp1 = new TimePeriodDBEntry(ua2.getId(), new Date(), 5, 1);
        TimePeriodTable.add(tp1);

        tp1 = new TimePeriodDBEntry(ua2.getId(), new Date(), 5, 1);
        TimePeriodTable.add(tp1);
    }
}
