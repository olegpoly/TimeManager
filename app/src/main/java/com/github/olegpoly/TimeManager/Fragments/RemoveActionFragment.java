package com.github.olegpoly.TimeManager.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.olegpoly.TimeManager.R;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.ActionTable;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.ActionDBEntry;

/**
 * User can remove user's activities on this fragment
 */
public class RemoveActionFragment extends Fragment {
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
            ActionDBEntry userActivityToBeRemoved = (ActionDBEntry) userActivitiesSelectorSpinner.getSelectedItem();
            ActionTable.remove(userActivityToBeRemoved);

            loadActivitiesIntoActivitiesSelectorSpinner();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_remove_action, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View thisView = getView();

        // initialize views
        Button removeActivityButton = (Button) thisView.findViewById(R.id.removeActivityButton);
        userActivitiesSelectorSpinner = (Spinner) thisView.findViewById(R.id.activitiesSelectorSpinner);

        // set the remove button listener
        removeActivityButton.setOnClickListener(removeActivityButtonClicked);
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
        ArrayAdapter<ActionDBEntry> activitiesAdaptor = new ArrayAdapter<>(getView().getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                ActionTable.getAll());

        userActivitiesSelectorSpinner.setAdapter(activitiesAdaptor);
    }
}
