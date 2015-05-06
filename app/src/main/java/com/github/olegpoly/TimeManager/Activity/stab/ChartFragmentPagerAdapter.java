package com.github.olegpoly.TimeManager.Activity.stab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.olegpoly.TimeManager.Fragments.BarChartFragment;
import com.github.olegpoly.TimeManager.Fragments.FilterFragment;
import com.github.olegpoly.TimeManager.Fragments.PieChartFragment;
import com.github.olegpoly.TimeManager.Interfaces.ChartFragment;
import com.github.olegpoly.TimeManager.ListCheckBox.ActionListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Filter;

public class ChartFragmentPagerAdapter extends FragmentStatePagerAdapter implements FilterFragment.FilterListener  {
    class NamedFragment {
        ChartFragment fragment;
        String name;

        NamedFragment(ChartFragment fragment, String name) {
            this.fragment = fragment;
            this.name = name;
        }
    }

    private List<NamedFragment> tabs2 = new ArrayList<>();

    public ChartFragmentPagerAdapter(FragmentManager fm) {
        super(fm);

        tabs2.add(new NamedFragment(new PieChartFragment(), "Pie Chart"));
        tabs2.add(new NamedFragment(new BarChartFragment(), "Bar Chart"));
    }

    @Override
    public int getCount() {
        return tabs2.size();
    }

    // Return the correct Fragment based on index
    @Override
    public Fragment getItem(int position) {
        return (Fragment)tabs2.get(position).fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Return the tab title to SlidingTabLayout
        return tabs2.get(position).name;
    }

    @Override
    public void filterApplied(FilterFragment filterFragment) {
        List<ActionListItem> actionList = filterFragment.listSetUp.actionList;

        List<ActionListItem> actionListCorrect = new Vector<>();

        for (ActionListItem actionListItem : actionList) {
            if (actionListItem.getIsSelected())
            actionListCorrect.add(actionListItem);
        }

        tabs2.get(0).fragment.changeFilter(actionListCorrect);
        tabs2.get(1).fragment.changeFilter(actionListCorrect);
    }
}