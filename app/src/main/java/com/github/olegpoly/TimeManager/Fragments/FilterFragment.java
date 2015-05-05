package com.github.olegpoly.TimeManager.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.olegpoly.TimeManager.ListCheckBox.ListSetUp;
import com.github.olegpoly.TimeManager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment {

    public interface FilterListener {
        void filterApplied(FilterFragment filterFragment);
    }

    public FilterListener filterListener;


    public View v;
    public ListSetUp listSetUp = new ListSetUp();

    public FilterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_filter, container, false);
            listSetUp.setContext(getActivity());
            listSetUp.setThisView(v);

        Button b = (Button) v.findViewById(R.id.applyFilterButton);
        final FilterFragment that = this;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterListener.filterApplied(that);
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
