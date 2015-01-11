package com.example.qwerty.timemanager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import TImeManagerDataBase.TimePeriodDBTableEntry;
import TImeManagerDataBase.UserActivityDB;
import TImeManagerDataBase.UserActivityDBTableEntry;

public class MainActivity extends Activity {
    /**
     * contains timers that need to maintain their values when the the phone sleeps
     */
    private ApplicationData appState;
    /**
     *  time span between two events: 1)start button clicked 2)stop button clicked
     */
    private TimeSpan startStopPeriod;
    /**
     * Represents the current session number. All time periods in database is associated with a
     * session number. To start a new timer - start a new session by using a value that hasn't been
     * used yet.
     */
    private long currentSessionNumber;
    /**
     * the service that manages the main timer
     */
    private TimerService timerService;
    /**
     * indicates whether timer service is bound or not
     */
    private boolean timerServiceBound;
    /**
     * runs in MainActivity's thread (not in the timer service).
     * Takes time from the service and displays it on the screen.
     */
    private Timer setPassedTimeTimer;
    /**
     * the textView for displaying the timer
     */
    private TextView timerTextView;
    /**
     * spinner for all user's activities
     */
    private Spinner activitiesSpinner;
    /**
     * TimerTask that displays time on the screen when called
     */
    private TimerTask displayTime = createDisplayTimeTimerTask();


    private ServiceConnection timerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            TimerService.LocalBinder binder = (TimerService.LocalBinder) service;
            timerService = binder.getService();
            timerServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            timerServiceBound = false;
        }
    };

    /**
     * Gets current time period from the timer service and displays on the screen
     */
    private TimerTask createDisplayTimeTimerTask() {
        TimerTask displayTime = new TimerTask() {
            @Override
            public void run() {
                long secs = timerService.getSecondsPassed();

                String timeString = getFormattedTimeString(secs);

                setTimerViewText(timeString);
            }
        };

        return displayTime;
    }

    /**
     * Load correct timer values when new user's activity is selected in the activities spinner.
     */
    AdapterView.OnItemSelectedListener activitiesSpinnerSelectedItemChanged  = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            loadDataForCurrentActivity();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parentView) {
        }

    };

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent timerServiceIntent = new Intent(this, TimerService.class);
        bindService(timerServiceIntent, timerServiceConnection, Context.BIND_AUTO_CREATE);

        // initialize pointers to view's elements
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        activitiesSpinner = (Spinner) findViewById(R.id.activitiesSpinner);

        // for testing porpoises, loads all table entries into the spinner
        loadTimePeriodsIntoSpinner();

        appState = ((ApplicationData) getApplicationContext());

        startStopPeriod = appState.getStartStopPeriod();

        UserActivityDB db = new UserActivityDB(this);

        currentSessionNumber = db.getLastSessionNumber();

        activitiesSpinner.setOnItemSelectedListener(activitiesSpinnerSelectedItemChanged);
    }

    /**
     * Load all data for the currently selected activity
     */
    private void loadDataForCurrentActivity() {
        UserActivityDB db = new UserActivityDB(activitiesSpinner.getContext());

        // get selected user's activity
        UserActivityDBTableEntry selectedUserActivity = (UserActivityDBTableEntry) activitiesSpinner.getSelectedItem();

        // get all time periods for the current session
        List<TimePeriodDBTableEntry> allPassedTimes = db.getAllPassedTimes(currentSessionNumber, selectedUserActivity.getId());

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

        if (timerService != null)
            timerService.addSeconds(difference);

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

    // only for testing purposes
    private void loadTimePeriodsIntoSpinner() {
        Spinner s = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<TimePeriodDBTableEntry> activitiesAdaptor = new ArrayAdapter<TimePeriodDBTableEntry>(this,
                android.R.layout.simple_spinner_dropdown_item, new UserActivityDB(this).getAllPassedTimes());

        s.setAdapter(activitiesAdaptor);
    }

    /**
    * Called when startTimerButton is clicked. Starts the service's timer.
    */
    private void startTimer() {
        if (timerServiceBound) {
            timerService.startTimer();

            if (setPassedTimeTimer != null) return;

            setPassedTimeTimer = new Timer();
            int startWhen = 0;
            int period = 1000;

            startStopPeriod.setStartDate(Calendar.getInstance());
            displayTime = createDisplayTimeTimerTask();

            setPassedTimeTimer.schedule(displayTime, startWhen, period);
        }
    }

    /**
    * Called when startTimerButton is clicked.
    * Stops the service's timer
    */
    private void stopTimer() {
        if (timerService != null)
        timerService.stopTimer();

        if (setPassedTimeTimer != null) {
            setPassedTimeTimer.cancel();
            setPassedTimeTimer.purge();
            setPassedTimeTimer = null;
        }

        UserActivityDB db = new UserActivityDB(this);

        startStopPeriod.setEndDate(Calendar.getInstance());

        long secondsPassedSinceTimerStarted = startStopPeriod.getDuration(TimeUnit.SECONDS);
        TimePeriodDBTableEntry tp = new TimePeriodDBTableEntry(new Date(), secondsPassedSinceTimerStarted);
        tp.setSessionNumber(currentSessionNumber);

        UserActivityDBTableEntry ua = (UserActivityDBTableEntry) activitiesSpinner.getSelectedItem();
        tp.setIdUserActivity(ua.getId());

        db.putNewTime(tp);

        startStopPeriod.reset();

        // for testing purposes, loads all table entries into the spinner
        loadTimePeriodsIntoSpinner();
    }

    /**
     * Set timerTextView's text on the ui thread
     * @param text the text to set
     */
    private void setTimerViewText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timerTextView.setText(text);
            }
        });
    }

    /**
     * When the manageActivities button is clicked, start the corresponding activity
     */
    private void manageActivitiesButton() {
        startActivity(new Intent(this, ManageActivities.class));
    }

    /**
     * display all user's activities in the activities spinner
     */
    private void loadActivitiesIntoActivitiesSpinner() {
        ArrayAdapter<UserActivityDBTableEntry> activitiesAdaptor = new ArrayAdapter<UserActivityDBTableEntry>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new UserActivityDB(this).getAllUserActivities());

        activitiesSpinner.setAdapter(activitiesAdaptor);
    }

    /**
     * Create new session. Reload the timer textView.
     */
    private void newSessionButtonClicked() {
        startNewSession();
        loadDataForCurrentActivity();
    }

    /**
     * Start a new session.
     */
    private void startNewSession() {
        currentSessionNumber++;
    }
}