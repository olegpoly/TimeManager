package com.github.olegpoly.TimeManager.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.github.olegpoly.TimeManager.Fragments.AddActionFragment;
import com.github.olegpoly.TimeManager.Fragments.NavigationDrawerFragment;
import com.github.olegpoly.TimeManager.Fragments.RemoveActionFragment;
import com.github.olegpoly.TimeManager.R;

/**
 * This FragmentActivity contain fragments for removing and adding activities
 */
public class ManageActionsActivity extends ActionBarActivity {
    /**
     * {inherit}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_action);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

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