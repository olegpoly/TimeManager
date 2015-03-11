package com.github.olegpoly.TimeManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.olegpoly.TimeManager.Activity.ChartsActivity;
import com.github.olegpoly.TimeManager.Activity.ManageActionsActivity;
import com.github.olegpoly.TimeManager.Core.ActionTimer;
import com.github.olegpoly.TimeManager.Core.ApplicationData;
import com.github.olegpoly.TimeManager.Core.SessionNumber;
import com.github.olegpoly.TimeManager.Core.TimeSpan;
import com.github.olegpoly.TimeManager.DataBaseExporter.DataBaseToJson;
import com.github.olegpoly.TimeManager.Fragments.NavigationDrawerFragment;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.ActionTable;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.TimePeriodTable;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.ActionDBEntry;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.TimePeriodDBEntry;
import com.github.olegpoly.TimeManager.UiUtils.UIApdater;

import java.io.File;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import com.github.olegpoly.TimeManager.Activity.BarChartActivityMultiDataset;
//import com.github.olegpoly.TimeManager.Activity.PieChartActivity;

public class MainActivity extends ActionBarActivity {
    /**
     * This timer interacts with the service's timer from an activity's context.
     */
    ActionTimer timer;
    /**
     * Indicates if the timer is running
     */
    boolean isTimerRunning = false;
    /**
     * Listener for item selection event from activitiesSpinner
     */
    AdapterView.OnItemSelectedListener activitiesSpinnerItemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!isTimerRunning)
                        loadDataForCurrentActivity();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };
    /**
     * the textView for displaying the timer
     */
    private TextView timerTextView;
    /**
     * spinner for all user's activities
     */
    private Spinner activitiesSpinner;
    /**
     * contains timers that need to maintain their values when the the phone sleeps
     */
    private ApplicationData appState;

    /**
     * This hook is called whenever an item in your options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

        }

        if (id == R.id.navigate) {
            Toast.makeText(this, "Hey", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);

        isTimerRunning = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // TODO: delete these test lines
        /*
        this.deleteDatabase(TimePeriodTable.TABLE_NAME);
        this.deleteDatabase(UserActivitiesTable.TABLE_NAME);
        UserActivityDB db = UserActivityDB.getInstance();
        UserActivityDB.deleteAll();
        UserActivityDB.addAll();
        */

        // initialize pointers to view's elements
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        activitiesSpinner = (Spinner) findViewById(R.id.activitiesSpinner);

        // for testing porpoises, loads all table entries into the spinner
        loadTimePeriodsIntoSpinner();

        appState = ((ApplicationData) getApplicationContext());

        UIApdater uiApdater = new UIApdater(this, timerTextView);
        timer = new ActionTimer(this, uiApdater);
        timer.startService();
        timer.bindTimer();

        activitiesSpinner.setOnItemSelectedListener(activitiesSpinnerItemSelectedListener);
    }

    /**
     * Load all data for the currently selected activity
     */
    public void loadDataForCurrentActivity() {
        // get selected user's activity
        ActionDBEntry selectedUserActivity = (ActionDBEntry) activitiesSpinner.getSelectedItem();

        SessionNumber sessionNumber = SessionNumber.getInstance();

        // get all time periods for the current session
        List<TimePeriodDBEntry> allPassedTimes = TimePeriodTable.getAll(sessionNumber.getCurrentSessionNumber(),
                selectedUserActivity.getId());

        long totalTime = 0; // total time of all time periods for the current session

        for (TimePeriodDBEntry timePeriod : allPassedTimes) {
            totalTime += timePeriod.getSecsPassed();
        }

        // set timerService's starting time and display it on the screen
        timer.setSecondsPassed(totalTime);
        String timeString = getFormattedTimeString(totalTime);
        setTimerViewText(timeString);
    }

    /**
     * Prepare timers and the service for work
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (isTimerRunning) {
            resumed();
            timer.bindTimer();
            timer.startTimer();
        }

        loadActivitiesIntoActivitiesSpinner();
    }

    /**
     * Prepare timers and the service for sleep
     */
    @Override
    protected void onStop() {
        stopped();
        timer.stopTimer();
        timer.unbindTimer();

        super.onStop();
    }

    /**
     * When the phone wakes up this app, calculate the time duration of sleep and add it to the timer
     */
    private void resumed() {
        TimeSpan timeInSleep = appState.getTimeInSleep();

        if (timeInSleep.isReseted()) {
            return;
        }

        timeInSleep.setEndDate(Calendar.getInstance());

        long difference = timeInSleep.getDuration(TimeUnit.SECONDS);

        if (timer != null)
            timer.addSeconds(difference);

        timeInSleep.reset();
    }

    /**
     * Save the time when the phone stops this app and it's timer service
     */
    private void stopped() {
        appState.getTimeInSleep().setStartDate(Calendar.getInstance());
    }

    /**
     * Creates a user friendly time string - h:m:s
     *
     * @param secs seconds
     * @return a user friendly time string
     */
    private String getFormattedTimeString(long secs) {
        long hours = TimeUnit.SECONDS.toHours(secs);
        long minutes = TimeUnit.SECONDS.toMinutes(secs) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.SECONDS.toSeconds(secs) - TimeUnit.HOURS.toSeconds(hours) -
                TimeUnit.MINUTES.toSeconds(minutes);

        String time = String.format("%02d : %02d : %02d", hours, minutes, seconds);

        return time;
    }

    // only for testing purposes
    private void loadTimePeriodsIntoSpinner() {
        Spinner s = (Spinner) findViewById(R.id.spinner);

        List<TimePeriodDBEntry> timePeriods = TimePeriodTable.getAll();
        List<String> timePeriodsStrings = new ArrayList<>();
        String userActivityName;

        for (TimePeriodDBEntry timePeriod : timePeriods) {
            try {
                userActivityName = ActionTable.get(timePeriod.getIdUserActivity()).getActivityName();
            } catch (SQLDataException e) {
                Log.e("database: ", "database test fails in MainActivity");
                continue;
            }

            timePeriodsStrings.add(timePeriod.toString() + " " + timePeriod.getId() + " " + userActivityName);
        }

        ArrayAdapter<String> activitiesAdaptor = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, timePeriodsStrings);

        s.setAdapter(activitiesAdaptor);
    }

    /**
     * Called when startTimerButton is clicked. Starts the service's timer.
     *
     * @param view the view that invoked this event
     */
    public void startTimer(View view) {
        timer.startTimer();
        isTimerRunning = true;
        activitiesSpinner.setEnabled(false);
    }

    /**
     * Called when startTimerButton is clicked.
     * Stops the service's timer
     *
     * @param view the view that invoked this event
     */
    public void stopTimer(View view) {
        ActionDBEntry ua = (ActionDBEntry) activitiesSpinner.getSelectedItem();

        timer.stopTimer(ua);

        isTimerRunning = false;
        activitiesSpinner.setEnabled(true);
        // for testing purposes, loads all table entries into the spinner
        loadTimePeriodsIntoSpinner();
    }

    /**
     * When the manageActivities button is clicked, start the corresponding activity
     *
     * @param view the view that invoked this event
     */
    public void manageActivitiesButton(View view) {
        startActivity(new Intent(this, ManageActionsActivity.class));
    }

    /**
     * display all user's activities in the activities spinner
     */
    private void loadActivitiesIntoActivitiesSpinner() {
        ArrayAdapter<ActionDBEntry> activitiesAdaptor = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                ActionTable.getAll());

        activitiesSpinner.setAdapter(activitiesAdaptor);
    }

    /**
     * Create new session. Reload the timer textView.
     *
     * @param view the view that invoked this event
     */
    public void newSessionButtonClicked(View view) {
        startNewSession();
        loadDataForCurrentActivity();
    }

    /**
     * Start a new session.
     */
    private void startNewSession() {
        SessionNumber.getInstance().startNewSession();
    }

    /**
     * Set timerTextView's text on the ui thread
     */
    public void setTimerViewText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timerTextView.setText(text);
            }
        });
    }

    // test
    // TODO: save file in an appropriate place
    public void databaseToXml(View view) {
        File sd = Environment.getExternalStorageDirectory();

       /*
       String path = sd + "/" + "test" + ".xml";
        UserActivityDB database = UserActivityDB.getInstance();
        DatabaseToXmlExporter xmlExporter = new DatabaseToXmlExporter(database.getWritableDatabase(), path);
        xmlExporter.exportData();
        */

        String path = sd + "/" + "test" + ".json";
        DataBaseToJson dataBaseToJson = new DataBaseToJson(path);
        dataBaseToJson.exportData();
    }

    public void openChart(View view) {
        startActivity(new Intent(this, ChartsActivity.class));
    }

    public void showList(View view) {
        //startActivity(new Intent(this, DemoActivity.class));
    }

    public void showPieChart23(View view) {
       // startActivity(new Intent(this, BarChartActivityMultiDataset.class));
    }
}