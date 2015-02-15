package com.github.olegpoly.TimeManager.Activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;

import com.github.olegpoly.TimeManager.ListCheckBox.TransformList;
import com.github.olegpoly.TimeManager.ListCheckBox.UserActivityListItem;
import com.github.olegpoly.TimeManager.R;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity
       // extends DemoBase мій комент
        extends FragmentActivity
        implements
       // OnSeekBarChangeListener,
        OnChartValueSelectedListener
{

    private PieChart mChart;
    MyCustomAdapter dataAdapter = null;

    private void displayListView() {

        //Array list of countries
        List<UserActivityListItem> countryList = TransformList.transform();

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this, R.layout.filter_row, countryList);
        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                UserActivityListItem country = (UserActivityListItem) parent.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + country.getUserActivityName(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }


    private class MyCustomAdapter extends ArrayAdapter<UserActivityListItem> {

        private List<UserActivityListItem> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId, List<UserActivityListItem> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.filter_row, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        UserActivityListItem country = (UserActivityListItem) cb.getTag();
                        country.setIsSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            UserActivityListItem country = countryList.get(position);
            holder.name.setText(country.getUserActivityName());
            //holder.name.setBackgroundColor(itColor1.next());
            holder.name.setChecked(country.getIsSelected());
            holder.name.setTag(country);

          //  if (country.getIsSelected())
           //     convertView.setBackgroundColor(itColor1.next());

            return convertView;

        }

    }


















    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_piechart);

        mChart = (PieChart) findViewById(R.id.chart1);

        // change the color of the center-hole
        mChart.setHoleColor(ColorTemplate.getHoloBlue());
       // mChart.setHoleColorTransparent(true);

        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setValueTypeface(tf);
        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));

        mChart.setHoleRadius(60f);

        mChart.setDescription("");

        mChart.setDrawYValues(true);
        mChart.setDrawCenterText(true);

        mChart.setDrawHoleEnabled(true);

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
        mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

        mChart.setCenterText("MPAndroidChart\nLibrary");

        setData(6);

        mChart.animateXY(1500, 1500);
        mChart.animateXY(1500, 1500);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);

        displayListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast.makeText(this, "onOptionsItemSelected", Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                if (mChart.isDrawYValuesEnabled())
                    mChart.setDrawYValues(false);
                else
                    mChart.setDrawYValues(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionTogglePercent: {
                if (mChart.isUsePercentValuesEnabled())
                    mChart.setUsePercentValues(false);
                else
                    mChart.setUsePercentValues(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHole: {
                if (mChart.isDrawHoleEnabled())
                    mChart.setDrawHoleEnabled(false);
                else
                    mChart.setDrawHoleEnabled(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionDrawCenter: {
                if (mChart.isDrawCenterTextEnabled())
                    mChart.setDrawCenterText(false);
                else
                    mChart.setDrawCenterText(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleXVals: {
                if (mChart.isDrawXValuesEnabled())
                    mChart.setDrawXValues(false);
                else
                    mChart.setDrawXValues(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionSave: {
                // mChart.saveToGallery("title"+System.currentTimeMillis());
                mChart.saveToPath("title" + System.currentTimeMillis(), "");
                break;
            }
            case R.id.animateX: {
                mChart.animateX(1800);
                break;
            }
            case R.id.animateY: {
                mChart.animateY(1800);
                break;
            }
            case R.id.animateXY: {
                mChart.animateXY(1800, 1800);
                break;
            }
        }
        return true;
    }

    private void setData(int count) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry((float) 2, i));
            xVals.add("par" + i);
        }

        PieDataSet set1 = new PieDataSet(yVals1, "Election Results");
        set1.setSliceSpace(20f);

        // add a lot of colors

       // ArrayList<Integer> colors = new ArrayList<Integer>();

        //for (int c : ColorTemplate.TEST_COLORS)
        //    colors.add(c);

        /*for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);*/

        //colors.add(ColorTemplate.getHoloBlue());

      // set1.setColors(colors);

        PieData data = new PieData(xVals, set1);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex) {
        Toast.makeText(this, "onValueSelected", Toast.LENGTH_SHORT).show();

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Toast.makeText(this, "onNothingSelected", Toast.LENGTH_SHORT).show();
        Log.i("PieChart", "nothing selected");
    }
}
