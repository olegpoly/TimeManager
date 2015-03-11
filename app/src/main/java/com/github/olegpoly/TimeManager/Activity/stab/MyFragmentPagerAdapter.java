package com.github.olegpoly.TimeManager.Activity.stab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.olegpoly.TimeManager.Fragments.BarChartFragment;
import com.github.olegpoly.TimeManager.Fragments.FilterFragment;
import com.github.olegpoly.TimeManager.Fragments.PieChartFragment;
import com.github.olegpoly.TimeManager.Fragments.RemoveActionFragment;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter implements FilterFragment.FilterListener  {
    PieChartFragment pcf;
    BarChartFragment bcf;

    // Holds tab titles
    private String tabTitles[] = new String[] { "Frag #1", "Frag #2" };
    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    // Return the correct Fragment based on index
    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            pcf = new PieChartFragment();
            return pcf;
        } else if(position == 1) {
            bcf = new BarChartFragment();
            return bcf;
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Return the tab title to SlidingTabLayout
        return tabTitles[position];
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void filterApplied() {
       pcf.setData(3);
        bcf.setData();
    }
}