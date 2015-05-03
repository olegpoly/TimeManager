package com.github.olegpoly.TimeManager.Core;

import com.github.olegpoly.TimeManager.Fragments.TimerFragment;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.ActionTable;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.TimePeriodTable;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.ActionDBEntry;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.TimePeriodDBEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleg on 02.05.2015.
 */
public class TimerFragementFactory {

   static public List<TimerFragment> getTimerFragments() {
        List<TimerFragment> fragments = new ArrayList<>();

        List<ActionDBEntry> allActivities = ActionTable.getAll();

        for (ActionDBEntry enty : allActivities) {
            TimerFragment fragment = new TimerFragment();
            fragment.setName(enty.getActivityName());

            long totalTime = 0;
            for (TimePeriodDBEntry x : TimePeriodTable.getAll(enty.getId())) {
                totalTime += x.getSecsPassed();
            }

            fragment.setTime(String.valueOf(totalTime));
            fragment.setThisEntry(enty);
            fragments.add(fragment);
        }

        return fragments;
    }

}
