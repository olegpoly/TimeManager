package com.github.olegpoly.TimeManager.Charts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.github.olegpoly.TimeManager.ListCheckBox.TransformList;
import com.github.olegpoly.TimeManager.ListCheckBox.UserActivityListItem;
import com.github.olegpoly.TimeManager.R;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.TimePeriodTable;
import com.github.olegpoly.TimeManager.androidcharts.PieHelper;
import com.github.olegpoly.TimeManager.androidcharts.PieView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Chart extends Activity {
    PieView pieView;
    MyCustomAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        pieView = (PieView)findViewById(R.id.pie_view);
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();
        pieView.setDate(pieHelperArrayList);
        pieView.selectedPie(2); //optional
        //pieView.setOnPieClickListener(listener); //optional
        pieView.showPercentLabel(false); //optional

        displayListView();
        checkButtonClick();
    }

    List<Integer> colors = new ArrayList<>();
    Iterator<Integer> itColor1;
    Iterator<Integer> itColor2;

    {
        colors.add(Color.BLUE);  //1
        colors.add(Color.GREEN);  //2
        colors.add(Color.YELLOW);  // 3
        colors.add(Color.CYAN);  // 4
        colors.add(Color.RED); // 5
        colors.add(Color.MAGENTA);  // 6
        colors.add(Color.BLACK); // 7

        itColor1 = colors.iterator();
        itColor2 = colors.iterator();
    }

    public void generate(View view) {
        itColor1 = colors.iterator();
        itColor2 = colors.iterator();

        checkButtonClick();
        //randomSet(pieView);
    }

    private void displayListView() {

        //Array list of countries
        List<UserActivityListItem> countryList = TransformList.transform();

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this, R.layout.country_info, countryList);
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

    private void checkButtonClick() {

        List<UserActivityListItem> countryList = dataAdapter.countryList;
        List<UserActivityListItem> listToPass = new ArrayList<>();
        Long id;
        List<Long> times = new ArrayList<>();

        for(int i=0;i<countryList.size();i++){
            UserActivityListItem ua = countryList.get(i);

            //if(ua.getIsSelected()){
                id = ua.getId();

                times.add(TimePeriodTable.getAllTime(id));
                listToPass.add(ua);
           //}
        }

        setPie(times, countryList);
        pieView.selectedPie(2);
    }

    public void setPie(List<Long> times, List<UserActivityListItem> uas) {
        Float total = 0F;
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<>();

        for (int i = 0; i < times.size(); ++i) {
            if (!uas.get(i).getIsSelected()) {
                continue;
            }

            total += times.get(i);
        }

        Float percent;

        Long l;

        for (int i = 0; i < times.size(); ++i) {
            if (!uas.get(i).getIsSelected()) {
                itColor2.next();
                continue;
            }

            l = times.get(i);

            percent = convertToPercent(total, l);
            PieHelper pi = new PieHelper( percent, itColor2.next() );

            pieHelperArrayList.add(pi);
        }

        pieView.showPercentLabel(true);

        pieView.setDate(pieHelperArrayList);
    }

    public Float convertToPercent(Float total, Long needed) {
        return (needed * 100) / total;
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
                convertView = vi.inflate(R.layout.country_info, null);

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

            if (country.getIsSelected())
            convertView.setBackgroundColor(itColor1.next());

            return convertView;

        }

    }
}
