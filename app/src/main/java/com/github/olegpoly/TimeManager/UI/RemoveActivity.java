package com.github.olegpoly.TimeManager.UI;

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
import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.UserActivitiesTable;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.UserActivityDBEntry;

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
            //UserActivityDB database = UserActivityDB.getInstance();

            UserActivityDBEntry userActivityToBeRemoved = (UserActivityDBEntry) userActivitiesSelectorSpinner.getSelectedItem();
            // database.removeUserActivity(userActivityToBeRemoved);
            UserActivitiesTable.remove(userActivityToBeRemoved);

            loadActivitiesIntoActivitiesSelectorSpinner();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_remove_user_activity, container, false);

        return rootView;
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(android.os.Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
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
        // UserActivityDB database = UserActivityDB.getInstance();

        //ArrayAdapter<UserActivityDBTableEntry> activitiesAdaptor = new ArrayAdapter<>(getView().getContext(),
        //        android.R.layout.simple_spinner_dropdown_item,
        //       database.getAllUserActivities());

        ArrayAdapter<UserActivityDBEntry> activitiesAdaptor = new ArrayAdapter<>(getView().getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                UserActivitiesTable.getAll());

        userActivitiesSelectorSpinner.setAdapter(activitiesAdaptor);
    }
}
