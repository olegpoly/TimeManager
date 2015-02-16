package com.github.olegpoly.TimeManager.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.github.olegpoly.TimeManager.ListCheckBox.TransformList;
import com.github.olegpoly.TimeManager.ListCheckBox.UserActivityListItem;
import com.github.olegpoly.TimeManager.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ListViewCheckboxesFragment extends Fragment {

    MyCustomAdapter dataAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        displayListView();

        return view;
    }

    private void displayListView() {
        //Array list of countries
        List<UserActivityListItem> countryList = TransformList.transform();

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(getActivity(), R.layout.filter_row, countryList);
        ListView listView = (ListView) getActivity().findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                UserActivityListItem country = (UserActivityListItem) parent.getItemAtPosition(position);

                Toast.makeText(getActivity(),
                        "Clicked on Row: " + country.getUserActivityName(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private class MyCustomAdapter extends ArrayAdapter<UserActivityListItem> {
        private List<UserActivityListItem> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId, List<UserActivityListItem> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<UserActivityListItem>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.filter_row, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        UserActivityListItem country = (UserActivityListItem) cb.getTag();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        country.setIsSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            UserActivityListItem country = countryList.get(position);
            holder.name.setText(country.getUserActivityName());
            holder.name.setChecked(country.getIsSelected());
            holder.name.setTag(country);

            return convertView;
        }
    }

    /*private void checkButtonClick() {
        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                List<UserActivityListItem> countryList = dataAdapter.countryList;
                for(int i=0;i<countryList.size();i++){
                    UserActivityListItem country = countryList.get(i);
                    if(country.getIsSelected()){
                        responseText.append("\n" + country.getUserActivityName());
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();
            }
        });
    }*/
}