package com.github.olegpoly.TimeManager.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.github.olegpoly.TimeManager.Fragments.AddActionFragment;
import com.github.olegpoly.TimeManager.Fragments.RemoveActionFragment;
import com.github.olegpoly.TimeManager.R;

/**
 * This FragmentActivity contain fragments for removing and adding activities
 */
public class ManageActionsActivity extends FragmentActivity {
    /**
     * Initialize FragmentTabHost and add to it tabs for adding and removing user activities
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_action);

        // prepare fragment host
        FragmentTabHost fragmentHost = (FragmentTabHost) findViewById(R.id.tabhost);
        fragmentHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // setup fragments for adding and removing user activities
        fragmentHost.addTab(fragmentHost.newTabSpec("tab1").setIndicator("add"),
                AddActionFragment.class, null);
        fragmentHost.addTab(fragmentHost.newTabSpec("tab2").setIndicator("remove"),
                RemoveActionFragment.class, null);
    }
}