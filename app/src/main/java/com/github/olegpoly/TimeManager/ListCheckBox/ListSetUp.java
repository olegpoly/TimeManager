package com.github.olegpoly.TimeManager.ListCheckBox;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.github.olegpoly.TimeManager.R;

import java.util.List;

public class ListSetUp {
    private FilterArrayAdapter dataAdapter = null;
    public List<ActionListItem> actionList;
    private View thisView;
    private Context context;

    public void displayListView() {
        //Array list of countries
        actionList = TransformList.transform();

        //create an ArrayAdaptar from the String Array
        dataAdapter = new FilterArrayAdapter(context, R.layout.filter_row, actionList);
        ListView listView = (ListView) this.thisView.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

    public View getThisView() {
        return thisView;
    }

    public void setThisView(View thisView) {
        this.thisView = thisView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public FilterArrayAdapter getDataAdapter() {
        return dataAdapter;
    }

    public void setDataAdapter(FilterArrayAdapter dataAdapter) {
        this.dataAdapter = dataAdapter;
    }
}
