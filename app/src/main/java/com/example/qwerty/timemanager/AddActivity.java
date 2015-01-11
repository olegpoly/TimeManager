package com.example.qwerty.timemanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import TImeManagerDataBase.UserActivityDB;
import TImeManagerDataBase.UserActivityDBTableEntry;

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
            String newUserActivityName = newActivityNameTextView.getText().toString();
            UserActivityDBTableEntry newUserActivity = new UserActivityDBTableEntry(newUserActivityName);

            UserActivityDB db = new UserActivityDB(getView().getContext());
            db.addUserActivity(newUserActivity);
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
