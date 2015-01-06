package com.example.qwerty.timemanager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;


public class ManageActivities extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user_activity);

        FragmentTabHost fragmentHost = (FragmentTabHost) findViewById(R.id.tabhost);
        fragmentHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        fragmentHost.addTab(fragmentHost.newTabSpec("tab1").setIndicator("add"),
                AddActivity.class, null);
        fragmentHost.addTab(fragmentHost.newTabSpec("tab2").setIndicator("remove"),
                RemoveActivity.class, null);
    }

}
