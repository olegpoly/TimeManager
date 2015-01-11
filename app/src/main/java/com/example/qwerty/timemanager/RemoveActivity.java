package com.example.qwerty.timemanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import TImeManagerDataBase.UserActivityDB;
import TImeManagerDataBase.UserActivityDBTableEntry;


public class RemoveActivity extends Fragment {
    Spinner userActivitiesSelectorSpinner;
    View.OnClickListener removeActivityButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserActivityDBTableEntry userActivityToBeRemoved = (UserActivityDBTableEntry) userActivitiesSelectorSpinner.getSelectedItem();
            new UserActivityDB(getView().getContext()).removeUserActivity(userActivityToBeRemoved);

            loadActivitiesIntoActivitiesSelectorSpinner();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_remove_user_activity, container, false);

        Button removeActivityButton = (Button) rootView.findViewById(R.id.removeActivityButton);
        removeActivityButton.setOnClickListener(removeActivityButtonClicked);

        userActivitiesSelectorSpinner = (Spinner) rootView.findViewById(R.id.activitiesSelectorSpinner);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadActivitiesIntoActivitiesSelectorSpinner();
    }

    public void loadActivitiesIntoActivitiesSelectorSpinner() {
        ArrayAdapter<UserActivityDBTableEntry> activitiesAdaptor = new ArrayAdapter<UserActivityDBTableEntry>(getView().getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new UserActivityDB(getView().getContext()).getAllUserActivities());

        userActivitiesSelectorSpinner.setAdapter(activitiesAdaptor);
    }
}
