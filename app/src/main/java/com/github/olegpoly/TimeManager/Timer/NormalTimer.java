package com.github.olegpoly.TimeManager.Timer;

import android.os.Handler;
import android.util.Log;

import com.github.olegpoly.TimeManager.Core.SessionNumber;
import com.github.olegpoly.TimeManager.Core.TimeSpan;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.TimePeriodTable;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.ActionDBEntry;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.TimePeriodDBEntry;
import com.github.olegpoly.TimeManager.UiUtils.UIApdater;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Oleg on 02.05.2015.
 */
public class NormalTimer {

    public void startTimer() {
        int startWhen = 0;
        int period = 1000;

        startStopPeriod = new TimeSpan();
        startStopPeriod.setStartDate(Calendar.getInstance());

        timerHandler = new Handler();
        timerHandler.postDelayed(mUpdateTimeTask, 1000);

        timerWorking = true;
    }

    private long secPassed = 0;
    private Handler timerHandler = new Handler();
    private TimeSpan startStopPeriod;
    private UIApdater timerTextViewSetter;
    private SessionNumber currentSessionNumber = SessionNumber.getInstance();
    public boolean timerWorking = false;

    public NormalTimer(UIApdater timerTextViewSetter) {
        this.timerTextViewSetter = timerTextViewSetter;
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            secPassed += 1;
            String timeString = getFormattedTimeString(secPassed);
            setTimerViewText(timeString);

            if (timerHandler != null)
                timerHandler.postDelayed(mUpdateTimeTask, 1000);
        }
    };

    public void stopTimer(ActionDBEntry userActivityEntry) {
        if (userActivityEntry == null) {
            Log.e("stopTimer:", "userActivityEntry = null");
            return;
        }

        if (!timerWorking) {
            return;
        }

        timerHandler.removeCallbacks(mUpdateTimeTask);
        timerHandler = null;

        startStopPeriod.setEndDate(Calendar.getInstance());

        long secondsPassedSinceTimerStarted = startStopPeriod.getDuration(TimeUnit.SECONDS);
        TimePeriodDBEntry timePeriodEntry = new TimePeriodDBEntry(new Date(), secondsPassedSinceTimerStarted);
        timePeriodEntry.setSessionNumber(currentSessionNumber.getCurrentSessionNumber());

        timePeriodEntry.setIdUserActivity(userActivityEntry.getId());

        TimePeriodTable.add(timePeriodEntry);

        startStopPeriod.reset();

        timerWorking = false;
    }

    private String getFormattedTimeString(long secs) {
        long hours = TimeUnit.SECONDS.toHours(secs);
        long minutes = TimeUnit.SECONDS.toMinutes(secs) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.SECONDS.toSeconds(secs) - TimeUnit.HOURS.toSeconds(hours) -
                TimeUnit.MINUTES.toSeconds(minutes);

        String time = String.format("%02d : %02d : %02d", hours, minutes, seconds);

        return time;
    }

    private void setTimerViewText(final String text) {
        timerTextViewSetter.setTextViewText(text);
    }
}
