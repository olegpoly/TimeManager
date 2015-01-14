package com.example.qwerty.timemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import TImeManagerDataBase.TimePeriodDBTableEntry;
import TImeManagerDataBase.UserActivityDB;
import TImeManagerDataBase.UserActivityDBTableEntry;

public class MainActivity extends Activity {
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
     * TODO: write documentation
     */
    ActivityTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize pointers to view's elements
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        activitiesSpinner = (Spinner) findViewById(R.id.activitiesSpinner);

        // for testing porpoises, loads all table entries into the spinner
        loadTimePeriodsIntoSpinner();

        appState = ((ApplicationData)getApplicationContext());

        timer = new ActivityTimer(this);
    }

    /**
     * Load all data for the currently selected activity
     * @param view the view that invoked this event
     */
    public void loadDataForCurrentActivity(View view) {
        UserActivityDB db = UserActivityDB.getInstance(this);

        // get selected user's activity
        UserActivityDBTableEntry selectedUserActivity = (UserActivityDBTableEntry) activitiesSpinner.getSelectedItem();

        SessionNumber sessionNumber = SessionNumber.getInstance();

        // get all time periods for the current session
        List<TimePeriodDBTableEntry> allPassedTimes = db.getAllTimePeriods(sessionNumber.getCurrentSessionNumber(),
                selectedUserActivity.getId());

        long totalTime = 0; // total time of all time periods for the current session

        for (TimePeriodDBTableEntry timePeriod : allPassedTimes) {
            totalTime += timePeriod.getSecsPassed();
        }

        // set timerService's starting time and display it on the screen
        timerService.setSecondsPassed(totalTime);
        String timeString = getFormattedTimeString(totalTime);
        setTimerViewText(timeString);
    }

    @Override
    protected void onStart() {
        resumed();
        super.onStart();
        loadActivitiesIntoActivitiesSpinner();
    }

    @Override
    protected void onResume() {
        resumed();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopped();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopped();
    }

    /**
     * When the phone wakes up this app, calculate the time duration of sleep and add it to the timer
     */
    private void resumed() {
        if (appState.getTimeInSleep() == null) return;

        Time now = new Time();
        now.setToNow();

        long difference = (now.toMillis(false) - appState.getTimeInSleep().toMillis(false)) / 1000;

        /*if (timerService != null)
            timerService.addSeconds(difference);*/

        appState.setTimeInSleep(null);
    }

    /**
     * Save the time when the phone stops this app and it's timer service
     */
    private void stopped() {
        Time now = new Time();
        now.setToNow();

        if (appState.getTimeInSleep() == null) {
            appState.setTimeInSleep(now);
            return;
        }

        if ((appState.getTimeInSleep().toMillis(false) - now.toMillis(false)) > 0) {
            appState.setTimeInSleep(now);
        }
    }

    /**
     * Creates a user friendly time string - h:m:s
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

        UserActivityDB database = UserActivityDB.getInstance(this);
        List<TimePeriodDBTableEntry> timePeriods = database.getAllTimePeriods();
        List<String> timePeriodsStrings = new ArrayList<>();
        String userActivityName = "non";

        for (TimePeriodDBTableEntry timePeriod : timePeriods) {
            try {
                userActivityName = database.getActivityById(timePeriod.getId()).getName();
            } catch (SQLDataException e) {
                //Log.e("database: ", e.getCause().toString());
                //continue;
            }

            timePeriodsStrings.add(timePeriod.toString() + " " + userActivityName);

            userActivityName = "non";
        }

        ArrayAdapter<String> activitiesAdaptor = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, timePeriodsStrings);

        s.setAdapter(activitiesAdaptor);
    }

    /**
    * Called when startTimerButton is clicked. Starts the service's timer.
    * @param view the view that invoked this event
    */
    public void startTimer(View view) {

    }

    /**
    * Called when startTimerButton is clicked.
    * Stops the service's timer
    * @param view the view that invoked this event
    */
    public void stopTimer(View view) {
        UserActivityDBTableEntry ua = (UserActivityDBTableEntry) activitiesSpinner.getSelectedItem();

        // for testing purposes, loads all table entries into the spinner
        loadTimePeriodsIntoSpinner();
    }

    /**
     * When the manageActivities button is clicked, start the corresponding activity
     * @param view the view that invoked this event
     */
    public void manageActivitiesButton(View view) {
        startActivity(new Intent(this, ManageActivities.class));
    }

    /**
     * display all user's activities in the activities spinner
     */
    private void loadActivitiesIntoActivitiesSpinner() {
        UserActivityDB database = UserActivityDB.getInstance(this);

        ArrayAdapter<UserActivityDBTableEntry> activitiesAdaptor = new ArrayAdapter<UserActivityDBTableEntry>(this,
                android.R.layout.simple_spinner_dropdown_item,
                database.getAllUserActivities());

        activitiesSpinner.setAdapter(activitiesAdaptor);
    }

    /**
     * Create new session. Reload the timer textView.
     * @param view the view that invoked this event
     */
    public void newSessionButtonClicked(View view) {
        startNewSession();
        loadDataForCurrentActivity(findViewById(R.id.loadActivityButton));
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
    private void setTimerViewText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timerTextView.setText(text);
            }
        });
    }
}