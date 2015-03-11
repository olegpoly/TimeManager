package com.github.olegpoly.TimeManager.Fragments;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.olegpoly.TimeManager.ListSetUp;
import com.github.olegpoly.TimeManager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment {

    public interface FilterListener {
        void filterApplied();
    }

    public FilterListener filterListener;


View v;
    ListSetUp listSetUp = new ListSetUp();

    public FilterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_filter, container, false);
            listSetUp.c = getActivity();
            listSetUp.v = v;

        Button b = (Button) v.findViewById(R.id.applyFilterButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterListener.filterApplied();
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // draw list with filter options
        listSetUp.displayListView();
    }
}
