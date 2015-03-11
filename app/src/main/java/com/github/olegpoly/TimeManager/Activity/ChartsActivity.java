package com.github.olegpoly.TimeManager.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.olegpoly.TimeManager.Activity.stab.ChartFragmentPagerAdapter;
import com.github.olegpoly.TimeManager.Activity.stab.SlidingTabLayout;
import com.github.olegpoly.TimeManager.Fragments.FilterFragment;
import com.github.olegpoly.TimeManager.R;

public class ChartsActivity extends FragmentActivity {
    /**
     * Called when a fragment is attached to the activity.
     *
     * @param fragment
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        // Layout manager that allows the user to flip through the pages
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // getSupportFragmentManager allows use to interact with the fragments
        // MyFragmentPagerAdapter will return a fragment based on an index that is passed
        ChartFragmentPagerAdapter chartFragmentPagerAdapter = new ChartFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(chartFragmentPagerAdapter);

        // Initialize the Sliding Tab Layout
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        // Connect the viewPager with the sliding tab layout
        slidingTabLayout.setViewPager(viewPager);

        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("gfkjffhf", "jhjhgjhg");
                //((SwipeRefreshLayout.OnRefreshListener)mPagerAdapter.getItem()).onRefresh();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });

        ////////
        FilterFragment ff = (FilterFragment)getSupportFragmentManager().findFragmentById(R.id.ff);
        ff.filterListener = chartFragmentPagerAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_charts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
