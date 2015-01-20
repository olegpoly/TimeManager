package TImeManagerDataBase;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Based on http://www.phonesdevelopers.com/1788273/
 * This class exports a database to an xml file
 */
public class DatabaseToXmlExporter {
    /**
     * Path of the xml file
      */
    private String xmlFilePath;
    /**
     * The database that is needed to export
     */
    private SQLiteDatabase database;
    /**
     * Creates xml file structure - rows, columns, etc.
     */
    private SqlToXmlWriter sqlToXmlWriter;

    /**
     * Constructor
     * @param database database to be exported
     * @param xmlFilePath path for the xml file
     */
    public DatabaseToXmlExporter(SQLiteDatabase database, String xmlFilePath) {
        this.setDatabase(database);
        this.setXmlFilePath(xmlFilePath);
    }

    /**
     * Creates file and streams that are needed for the export
     */
    public void prepareXmlFile() {
        try {
            // create a file on the sdcard to export the
            // database contents to
            File myFile = new File(xmlFilePath);
            myFile.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(myFile);
            BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream);

            sqlToXmlWriter = new SqlToXmlWriter(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exports data to the xml file
     */
    public void exportData() {
        prepareXmlFile();

        try {
            sqlToXmlWriter.startDatabaseExport(database.getPath());

            // get the tables out of the given sqlite database
            String sql = "SELECT * FROM sqlite_master";

            Cursor cur = database.rawQuery(sql, new String[0]);
            cur.moveToFirst();

            String tableName;
            while (cur.getPosition() < cur.getCount()) {
                tableName = cur.getString(cur.getColumnIndex("name"));

                // don't process these two tables since they are used
                // for metadata
                if (!tableName.equals("android_metadata")
                        && !tableName.equals("sqlite_sequence")) {
                    exportTable(tableName);
                }

                cur.moveToNext();
            }
            sqlToXmlWriter.endDatabaseExport();
            sqlToXmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Export a table to the xml file
     * @param tableName the table's name
     * @throws IOException if there was a problem with the file or storage
     */
    private void exportTable(String tableName) throws IOException {
        sqlToXmlWriter.startTable(tableName);

        // get everything from the table
        String getAllEntriesSqlCommand = "select * from " + tableName;
        Cursor cursor = database.rawQuery(getAllEntriesSqlCommand, new String[0]);
        int columnCount = cursor.getColumnCount();

        cursor.moveToFirst();

        // move through the table, creating rows
        // and adding each column with name and value
        // to the row
        while (cursor.getPosition() < cursor.getCount()) {
            sqlToXmlWriter.startRow();
            String columnName;
            String columnValue;
            for (int idx = 0; idx < columnCount; idx++) {
                columnName = cursor.getColumnName(idx);
                columnValue = cursor.getString(idx);
                sqlToXmlWriter.addColumn(columnName, columnValue);
            }

            sqlToXmlWriter.endRow();
            cursor.moveToNext();
        }

        cursor.close();

        sqlToXmlWriter.endTable();
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

    /**
     * Get the database used for exportation
     * @return
     */
    public SQLiteDatabase getDatabase() {
        return database;
    }

    /**
     * Set the database that must be exported
     * @param database the database that must be exported
     */
    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    /**
     * Creates xml file structure - rows, columns, etc.
     */
    private class SqlToXmlWriter {
        private static final String CLOSING_WITH_TICK = "'>";
        private static final String START_DB = "<export-database name='";
        private static final String END_DB = "</export-database>";
        private static final String START_TABLE = "<table name='";
        private static final String END_TABLE = "</table>";
        private static final String START_ROW = "<row>";
        private static final String END_ROW = "</row>";
        private static final String START_COL = "<col name='";
        private static final String END_COL = "</col>";

        /**
         * Stream for writing to the xml file
         */
        private BufferedOutputStream outputStream;

        /**
         * Default constructor
         * @throws FileNotFoundException if there was a problem with the file or storage
         */
        public SqlToXmlWriter() throws FileNotFoundException {
            this(new BufferedOutputStream(new FileOutputStream(xmlFilePath)));
        }

        /**
         * Constructor
         * @param outputStream Stream for writing to the xml file
         */
        public SqlToXmlWriter(BufferedOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        /**
         * Closes the stream for writing to the xml file
         * @throws IOException if there was a problem with the file or storage
         */
        public void close() throws IOException {
            if (outputStream != null) {
                outputStream.close();
            }
        }

        /**
         * Writes the opening part of the xml file
         * @param databaseName name of the database that is to be exported
         * @throws IOException if there was a problem with the file or storage
         */
        public void startDatabaseExport(String databaseName) throws IOException {
            String stg = START_DB + databaseName + CLOSING_WITH_TICK;
            outputStream.write(stg.getBytes());
        }

        /**
         * Writes the losing part of the xml file
         * @throws IOException if there was a problem with the file or storage
         */
        public void endDatabaseExport() throws IOException {
            outputStream.write(END_DB.getBytes());
        }

        /**
         * Writes the opening xml tag for a table
         * @param tableName table's name
         * @throws IOException if there was a problem with the file or storage
         */
        public void startTable(String tableName) throws IOException {
            String stg = START_TABLE + tableName + CLOSING_WITH_TICK;
            outputStream.write(stg.getBytes());
        }

        /**
         * Writes the closing xml tag for a table
         * @throws IOException if there was a problem with the file or storage
         */
        public void endTable() throws IOException {
            outputStream.write(END_TABLE.getBytes());
        }

        /**
         * Writes the starting xml tag for a row
         * @throws IOException if there was a problem with the file or storage
         */
        public void startRow() throws IOException {
            outputStream.write(START_ROW.getBytes());
        }

        /**
         * Writes the ending xml tag for a row
         * @throws IOException if there was a problem with the file or storage
         */
        public void endRow() throws IOException {
            outputStream.write(END_ROW.getBytes());
        }

        /**
         * Writes the column start tag + column value + column ent tag.
         * This function doesn't format the column value with xml tags, just writes it the file.
         * @param columnName
         * @param columnValue value of the new column, it must be already formatted with xml tags.
         * @throws IOException if there was a problem with the file or storage
         */
        public void addColumn(String columnName, String columnValue) throws IOException {
            String stg = START_COL + columnName + CLOSING_WITH_TICK + columnValue + END_COL;
            outputStream.write(stg.getBytes());
        }
    }

}