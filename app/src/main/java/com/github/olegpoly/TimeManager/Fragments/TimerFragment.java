package com.github.olegpoly.TimeManager.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.olegpoly.TimeManager.Core.ActionTimer;
import com.github.olegpoly.TimeManager.Core.TimeSpan;
import com.github.olegpoly.TimeManager.R;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.ActionDBEntry;
import com.github.olegpoly.TimeManager.Timer.NormalTimer;
import com.github.olegpoly.TimeManager.UiUtils.UIApdater;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment {

    TextView activityName;
    TextView timePassed;
    Button startTimer;
    Button stopTimer;

    String name;
    String time;

    public TimerFragment() {
        // Required empty public constructor
    }

    NormalTimer timer;
    Boolean isTimerRunning;
    ActionDBEntry thisEntry;

    View.OnClickListener start = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            timer.startTimer();
            isTimerRunning = true;
        }
    };

    View.OnClickListener stop = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            timer.stopTimer(thisEntry);

            isTimerRunning = false;
        }
    };

    static int id = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        startTimer = (Button) view.findViewById(R.id.startTimer);
        startTimer.setOnClickListener(start);
        stopTimer = (Button) view.findViewById(R.id.stopTimer);
        stopTimer.setOnClickListener(stop);

        activityName = (TextView) view.findViewById(R.id.activityName);
        activityName.setText(name);
        timePassed = (TextView) view.findViewById(R.id.timeTextView);
        timePassed.setText(time);

        view.setId(id);
        id++;

        UIApdater uiApdater = new UIApdater(this.getActivity(), timePassed);
        timer = new NormalTimer(uiApdater);

        return view;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ActionDBEntry getThisEntry() {
        return thisEntry;
    }

    public void setThisEntry(ActionDBEntry thisEntry) {
        this.thisEntry = thisEntry;
    }
}
