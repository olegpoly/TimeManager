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

/**
 * User can remove user's activities on this fragment
 */
public class RemoveActivity extends Fragment {
    /**
     * the spinner for displaying user's activities
     */
    Spinner userActivitiesSelectorSpinner;

    /**
     * removes selected in the spinner activity form database
     */
    View.OnClickListener removeActivityButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserActivityDB database = UserActivityDB.getInstance();

            UserActivityDBTableEntry userActivityToBeRemoved = (UserActivityDBTableEntry) userActivitiesSelectorSpinner.getSelectedItem();
            database.removeUserActivity(userActivityToBeRemoved);

            loadActivitiesIntoActivitiesSelectorSpinner();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_remove_user_activity, container, false);

        // initialize views
        Button removeActivityButton = (Button) rootView.findViewById(R.id.removeActivityButton);
        userActivitiesSelectorSpinner = (Spinner) rootView.findViewById(R.id.activitiesSelectorSpinner);

        // set the remove button listener
        removeActivityButton.setOnClickListener(removeActivityButtonClicked);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadActivitiesIntoActivitiesSelectorSpinner();
    }

    /**
     * load a list of user's activities into the spinner
     */
    public void loadActivitiesIntoActivitiesSelectorSpinner() {
        UserActivityDB database = UserActivityDB.getInstance();

        ArrayAdapter<UserActivityDBTableEntry> activitiesAdaptor = new ArrayAdapter<>(getView().getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                database.getAllUserActivities());

        userActivitiesSelectorSpinner.setAdapter(activitiesAdaptor);
    }
}
