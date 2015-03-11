package com.github.olegpoly.TimeManager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.olegpoly.TimeManager.ListCheckBox.TransformList;
import com.github.olegpoly.TimeManager.ListCheckBox.UserActivityListItem;

import java.util.ArrayList;
import java.util.List;

public class ListSetUp {
    public PieChart mChart;
    MyCustomAdapter dataAdapter = null;
    public List<UserActivityListItem> countryList;
    public View v;

    public Context c;

    public class MyCustomAdapter extends ArrayAdapter<UserActivityListItem> {
        public List<UserActivityListItem> countryList;

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
                LayoutInflater vi = (LayoutInflater) c.getSystemService(
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

    public void displayListView() {
        //Array list of countries
        countryList = TransformList.transform();

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(c, R.layout.filter_row, countryList);
        ListView listView = (ListView) v.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                UserActivityListItem country = (UserActivityListItem) parent.getItemAtPosition(position);

                Toast.makeText(c.getApplicationContext(),
                        "Clicked on Row: " + country.getUserActivityName(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
}
