package TImeManagerDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qwerty.timemanager.ApplicationData;

import java.util.Date;

import TImeManagerDataBase.Table.TimePeriodTable;
import TImeManagerDataBase.Table.UserActivitiesTable;
import TImeManagerDataBase.TableEntry.TimePeriodDBTableEntry;
import TImeManagerDataBase.TableEntry.UserActivityDBTableEntry;

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

    static public void deleteAll() {
        TimePeriodTable.DropIfExists();
        UserActivitiesTable.DropIfExists();

        // recreate tables
        UserActivitiesTable.createTable();
        TimePeriodTable.createTable();
    }

    static public void addAll() {
        UserActivityDBTableEntry ua1 = new UserActivityDBTableEntry("ac1");
        UserActivitiesTable.add(ua1);
        UserActivityDBTableEntry ua2 = new UserActivityDBTableEntry("ac2");
        UserActivitiesTable.add(ua2);

        TimePeriodDBTableEntry tp1 = new TimePeriodDBTableEntry(ua1.getId(), new Date(), 5, 0);
        TimePeriodTable.add(tp1);

        tp1 = new TimePeriodDBTableEntry(ua2.getId(), new Date(), 5, 0);
        TimePeriodTable.add(tp1);

        tp1 = new TimePeriodDBTableEntry(ua1.getId(), new Date(), 5, 1);
        TimePeriodTable.add(tp1);

        tp1 = new TimePeriodDBTableEntry(ua2.getId(), new Date(), 5, 1);
        TimePeriodTable.add(tp1);

        tp1 = new TimePeriodDBTableEntry(ua2.getId(), new Date(), 5, 1);
        TimePeriodTable.add(tp1);
    }
}
