package com.github.olegpoly.TimeManager.Activity;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.olegpoly.TimeManager.DataBaseExporter.DataBaseToJson;
import com.github.olegpoly.TimeManager.DataBaseExporter.DatabaseToXml;
import com.github.olegpoly.TimeManager.R;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.DataBase;

import java.io.File;

public class ExportsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exports);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exports, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void exportToJSON(View view) {
        File sd = Environment.getExternalStorageDirectory();

        String path = sd + "/" + "test" + ".json";
        DataBaseToJson dataBaseToJson = new DataBaseToJson(path);
        dataBaseToJson.exportData();
    }

    public void exportToXML(View view) {
        File sd = Environment.getExternalStorageDirectory();
        String path = sd + "/" + "test" + ".xml";
        DatabaseToXml xmlExporter = new DatabaseToXml(DataBase.getInstance().getWritableDatabase(), path);
        xmlExporter.exportData();
    }
}
