package com.github.olegpoly.TimeManager.ListCheckBox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.github.olegpoly.TimeManager.R;

import java.util.ArrayList;
import java.util.List;

public class FilterArrayAdapter extends ArrayAdapter<ActionListItem> {
    public List<ActionListItem> countryList;
    public Context context;

    public FilterArrayAdapter(Context context, int textViewResourceId, List<ActionListItem> countryList) {
        super(context, textViewResourceId, countryList);

        this.context = context;
        this.countryList = new ArrayList<>();
        this.countryList.addAll(countryList);
    }

    private class ViewHolder {
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.filter_row, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(viewHolder);

            viewHolder.name.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    ActionListItem country = (ActionListItem) cb.getTag();
                    country.setIsSelected(cb.isChecked());
                }
            });
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ActionListItem action = countryList.get(position);
        viewHolder.name.setText(action.getUserActivityName());
        viewHolder.name.setChecked(action.getIsSelected());
        viewHolder.name.setTag(action);

        return convertView;
    }
}
