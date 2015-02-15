package com.github.olegpoly.TimeManager.DataBaseExporter;

import com.github.olegpoly.TimeManager.DataBaseExporter.TypeAdapters.ActionAdapter;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.ActionTable;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.ActionDBEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by root on 08.02.15.
 */
public class DataBaseToJson {
    /**
     * Path of the xml file
     */
    private String xmlFilePath;

    BufferedOutputStream outputStream;


    public DataBaseToJson(String xmlFilePath) {
        this.setXmlFilePath(xmlFilePath);
    }

    /**
     * Creates file and streams that are needed for the export
     */
    public void prepareFile() {
        try {
            // create a file on the sdcard to export the
            // database contents to
            File myFile = new File(xmlFilePath);
            myFile.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(myFile);
            outputStream = new BufferedOutputStream(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exports data to the xml file
     */
    public void exportData() {
        prepareFile();

        try {
            exportTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Export a table to the xml file
     * @throws IOException if there was a problem with the file or storage
     */
    private void exportTable() throws IOException {
        ActionTable.getAll();

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ActionDBEntry.class, new ActionAdapter());

        Gson gson = builder.create();
        String json = "";

        json = gson.toJson(ActionTable.getAll());
        outputStream.write(json.getBytes());

        outputStream.flush();
        outputStream.close();
    }



    /**
     * Get the path to the xml file
     * @return the path to the xml file
     */
    public String getXmlFilePath() {
        return xmlFilePath;
    }

    /**
     * Set the path to the xml file
     * @param xmlFilePath the path to the xml file
     */
    public void setXmlFilePath(String xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }
}
