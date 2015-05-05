package com.github.olegpoly.TimeManager.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.LargeValueFormatter;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.github.olegpoly.TimeManager.Interfaces.ChartFragment;
import com.github.olegpoly.TimeManager.ListCheckBox.ActionListItem;
import com.github.olegpoly.TimeManager.R;

import java.util.ArrayList;
import java.util.List;

public class BarChartFragment extends Fragment implements OnChartValueSelectedListener, ChartFragment {
    private BarChart mChart;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mChart = (BarChart) v.findViewById(R.id.chartBar);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDescription("");

        // disable the drawing of values
        mChart.setDrawYValues(false);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setValueFormatter(new LargeValueFormatter());

        mChart.setDrawBarShadow(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawHorizontalGrid(false);;

        XLabels xl  = mChart.getXLabels();
        xl.setCenterXLabelText(true);

        YLabels yl = mChart.getYLabels();
        yl.setFormatter(new LargeValueFormatter());
    }

    @Override
    public void changeFilter(List<ActionListItem> actionList) {
        int xProg = 2;
        int yProg = 2;

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < xProg; i++) {
            xVals.add((i+1990) + "");
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();

        float mult = yProg * 10000000f;

        for (int i = 0; i < xProg; i++) {
            float val = (float) (Math.random() * mult) + 3;
            yVals1.add(new BarEntry(val, i));
        }

        for (int i = 0; i < xProg; i++) {
            float val = (float) (Math.random() * mult) + 3;
            yVals2.add(new BarEntry(val, i));
        }

        for (int i = 0; i < xProg; i++) {
            float val = (float) (Math.random() * mult) + 3;
            yVals3.add(new BarEntry(val, i));
        }

        // create 3 datasets with different types
        BarDataSet set1 = new BarDataSet(yVals1, "Sport");
//        set1.setColors(ColorTemplate.createColors(getApplicationContext(), ColorTemplate.FRESH_COLORS));
        set1.setColor(Color.rgb(104, 241, 175));
        BarDataSet set2 = new BarDataSet(yVals2, "Study");
        set2.setColor(Color.rgb(164, 228, 251));
        BarDataSet set3 = new BarDataSet(yVals3, "Rest");
        set3.setColor(Color.rgb(242, 247, 158));

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        BarData data = new BarData(xVals, dataSets);

        // add space between the dataset groups in percent of bar-width
        data.setGroupSpace(110f);

        mChart.setData(data);
        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }
}
