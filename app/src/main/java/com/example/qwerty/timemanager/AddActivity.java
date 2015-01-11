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


public class AddActivity extends Fragment {
    TextView newActivityNameTextView;
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

        Button addActivityButton = (Button) thisView.findViewById(R.id.AddActivityButton);
        addActivityButton.setOnClickListener(OnClickAddActivityButton);

        newActivityNameTextView = (TextView) thisView.findViewById(R.id.newActivityNameEditText);

        return thisView;
    }
}
