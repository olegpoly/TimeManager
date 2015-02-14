package com.github.olegpoly.TimeManager.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.olegpoly.TimeManager.R;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.UserActivitiesTable;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.UserActivityDBEntry;

/**
 * this fragment lets user add user's activities
 */
public class AddActivity extends Fragment {
    /**
     * text view for new activity name
     */
    TextView newActivityNameTextView;

    /**
     * create new activity on button click
     */
    View.OnClickListener OnClickAddActivityButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get new name from the textView
            String newUserActivityName = newActivityNameTextView.getText().toString();

            // check if the entered name already exists
            //UserActivityDB database = UserActivityDB.getInstance();
            //boolean alreadyExists = database.checkIfActivityExists(newUserActivityName);
            boolean alreadyExists = UserActivitiesTable.checkIfExists(newUserActivityName);

            // if activity already exists - return from function
            if (alreadyExists) {
                Toast.makeText(getActivity().getApplicationContext(), "this activity already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            UserActivityDBEntry newUserActivity = new UserActivityDBEntry(newUserActivityName);

            // add activity to database and clear the textView
            //database.addUserActivity(newUserActivity);
            UserActivitiesTable.add(newUserActivity);
            newActivityNameTextView.setText("");
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.activity_add_user_activity, container, false);

        // initialize views
        Button addActivityButton = (Button) thisView.findViewById(R.id.AddActivityButton);
        newActivityNameTextView = (TextView) thisView.findViewById(R.id.newActivityNameEditText);

        // set the addActivity's button onClick listener
        addActivityButton.setOnClickListener(OnClickAddActivityButton);

        return thisView;
    }
}
