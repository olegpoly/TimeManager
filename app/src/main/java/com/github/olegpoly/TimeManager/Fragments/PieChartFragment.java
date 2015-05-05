package com.github.olegpoly.TimeManager.Fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.olegpoly.TimeManager.Interfaces.ChartFragment;
import com.github.olegpoly.TimeManager.ListCheckBox.ActionListItem;
import com.github.olegpoly.TimeManager.ListCheckBox.ListSetUp;
import com.github.olegpoly.TimeManager.R;

import java.util.ArrayList;
import java.util.List;

public class PieChartFragment extends Fragment implements OnChartValueSelectedListener, ChartFragment {
    private PieChart mChart;
    View thisView;

    public PieChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        return thisView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mChart = (PieChart) thisView.findViewById(R.id.chart1);

        // change the color of the center-hole
        mChart.setHoleColor(ColorTemplate.getHoloBlue());
        // mChart.setHoleColorTransparent(true);

        mChart.setHoleRadius(60f);

        mChart.setDescription("");

        mChart.setDrawYValues(true);
        //mChart.setDrawCenterText(true);
        //mChart.setDrawHoleEnabled(true);

        mChart.setRotationAngle(0);

        // draws the corresponding description value into the slice
        mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);

        // display percentage values
        mChart.setUsePercentValues(true);
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        //mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

        //mChart.setCenterText("MPAndroidChart\nLibrary");

       // listSetUp.setData(listSetUp.countryList.size());
        //changeFilter(2);

        mChart.animateXY(1500, 1500);
        mChart.animateXY(1500, 1500);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
//        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(5f);
    }

    @Override
    public void changeFilter(List<ActionListItem> actionList) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < actionList.size(); i++) {
            yVals1.add(new Entry((float) 2, i+1));
            xVals.add(actionList.get(i).getUserActivityName());
        }

        PieDataSet set1 = new PieDataSet(yVals1, "Activities");
        set1.setSliceSpace(20f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        set1.setColors(colors);

        PieData data = new PieData(xVals, set1);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex) {
        if (e == null)
            return;
    }

    @Override
    public void onNothingSelected() {
    }
}


