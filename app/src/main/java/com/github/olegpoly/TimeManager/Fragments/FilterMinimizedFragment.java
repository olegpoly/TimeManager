package com.github.olegpoly.TimeManager.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.olegpoly.TimeManager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterMinimizedFragment extends Fragment {


    public FilterMinimizedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_minimized, container, false);
    }


}
