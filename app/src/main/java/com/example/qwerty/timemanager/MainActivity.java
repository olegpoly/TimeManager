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

public class MainActivity extends Activity {
    ApplicationData appState;
    TimeSpan startStopPeriod;
    long currentSessionNumber;
    // It takes time from the timer service and displays it in this view
    private TimerService timerService;  // service that manages the timer
    private boolean timerServiceBound;  // indicates whether timer service is bound or not
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
    private Timer setPassedTimeTimer;  // timer that runs in MainActivity's thread (not in the timer service).
    private TextView timerTextView;  // the textView for displaying the timer
    private Spinner activitiesSpinner; // spinner that shows all user's activities
    private TimerTask displayTime = createDisplayTimeTimerTask();

    private TimerTask createDisplayTimeTimerTask() {
        TimerTask displayTime = new TimerTask() {
            @Override
            public void run() {
                long secs = timerService.getTime();

                String timeString = getFormattedTimeString(secs);

                setTimerViewText(timeString);
            }
        };

        return displayTime;
    }

    AdapterView.OnItemSelectedListener activitiesSpinnerSelectedItemChanged  = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            selectedItemChanged(parentView);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parentView) {
            // your code here
        }

    };

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

        loadTimePeriodsIntoSpinner();

        appState = ((ApplicationData) getApplicationContext());

        startStopPeriod = appState.getStartStopPeriod();

        UserActivityDB db = new UserActivityDB(this);

        currentSessionNumber = db.getLastSessionNumber();

        activitiesSpinner.setOnItemSelectedListener(activitiesSpinnerSelectedItemChanged);
    }

    public void selectedItemChanged(AdapterView<?> parentView) {
        UserActivityDB db = new UserActivityDB(parentView.getContext());

        UserActivityDBTableEntry selectedUserActivity = (UserActivityDBTableEntry) activitiesSpinner.getSelectedItem();

        List<TimePeriodDBTableEntry> allPassedTimes = db.getAllPassedTimes(currentSessionNumber, selectedUserActivity.getId());

        long totalTime = 0;

        for (TimePeriodDBTableEntry timePeriod : allPassedTimes) {
            totalTime += timePeriod.getSecsPassed();
        }

        timerService.setTime(totalTime);

        String timeString = getFormattedTimeString(totalTime);

        setTimerViewText(timeString);
    }

    @Override
    protected void onStart() {
        setResumed();
        super.onStart();
        loadActivitiesIntoActivitiesSpinner();
    }

    @Override
    protected void onResume() {
        setResumed();
        super.onResume();
    }

    void setResumed() {
        if (appState.getTimeInSleep() == null) return;

        Time now = new Time();
        now.setToNow();

        long difference = (now.toMillis(false) - appState.getTimeInSleep().toMillis(false)) / 1000;

        if (timerService != null)
            timerService.addTime(difference);

        appState.setTimeInSleep(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStopped();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setStopped();
    }

    void setStopped() {
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

    public void loadTimePeriodsIntoSpinner() {
        Spinner s = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<TimePeriodDBTableEntry> activitiesAdaptor = new ArrayAdapter<TimePeriodDBTableEntry>(this,
                android.R.layout.simple_spinner_dropdown_item, new UserActivityDB(this).getAllPassedTimes());

        s.setAdapter(activitiesAdaptor);
    }

    // Called when startTimerButton is clicked.
    // Starts timer
    public void startTimer(View v) {
        if (timerServiceBound) {
            timerService.startTimer();

            if (setPassedTimeTimer != null) return;

            setPassedTimeTimer = new Timer();
            int startWhen = 0;
            int period = 1000;

            //startStopPeriod.setSeconds(timerService.getTime());
            startStopPeriod.setStartDate(Calendar.getInstance());
            displayTime = createDisplayTimeTimerTask();

            setPassedTimeTimer.schedule(displayTime, startWhen, period);
        }
    }

    // Called when startTimerButton is clicked.
    // Stops timer
    public void stopTimer(View view) {
        timerService.stopTimer();

        setPassedTimeTimer.cancel();
        setPassedTimeTimer.purge();
        setPassedTimeTimer = null;

        UserActivityDB db = new UserActivityDB(this);

        startStopPeriod.setEndDate(Calendar.getInstance());

        long secondsPassedSinceTimerStarted = startStopPeriod.getDuration(TimeUnit.SECONDS);
        TimePeriodDBTableEntry tp = new TimePeriodDBTableEntry(new Date(), secondsPassedSinceTimerStarted);
        tp.setSessionNumber(currentSessionNumber);

        UserActivityDBTableEntry ua = (UserActivityDBTableEntry) activitiesSpinner.getSelectedItem();
        tp.setIdUserActivity(ua.getId());

        db.putNewTime(tp);

        startStopPeriod.reset();
    }

    // Set timerTextView's text
    public void setTimerViewText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timerTextView.setText(text);
            }
        });
    }

    public void manageActivitiesButton(View view) {
        startActivity(new Intent(this, ManageActivities.class));
    }

    public void loadActivitiesIntoActivitiesSpinner() {
        ArrayAdapter<UserActivityDBTableEntry> activitiesAdaptor = new ArrayAdapter<UserActivityDBTableEntry>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new UserActivityDB(this).getAllUserActivities());

        activitiesSpinner.setAdapter(activitiesAdaptor);
    }

    // Create new session. Reload the timer textView.
    public void newSessionButtonClicked(View view) {
        currentSessionNumber++;
        selectedItemChanged(activitiesSpinner);
    }
}