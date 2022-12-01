package com.cydeo.utilities;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DB_util {

    // declaring at class level so all methods can access
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private static ResultSetMetaData metaData;


    /**
     * Create Connection by jdbc url and username , password provided
     *
     * @param url      jdbc url for any database
     * @param username username for database
     * @param password password for database
     */
    public static void createConnection(String url, String username, String password) {

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("CONNECTION SUCCESSFUL");
        } catch (Exception e) {
            System.out.println("CONNECTION HAS FAILED " + e.getMessage());
        }
    }

    /**
     * Create connection method , just checking one connection successful or not
     */
    public static void createConnection() {

        String url      = ConfigurationReader.getProperty("db.url") ;
        String username = ConfigurationReader.getProperty("db.username") ;
        String password = ConfigurationReader.getProperty("db.password") ;

        createConnection(url, username, password);

    }

    /**
     * Run the sql query provided and return ResultSet object
     *
     * @param sql the query to run
     * @return ResultSet object  that contains data
     */
    public static ResultSet runQuery(String sql) {

        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(sql); // setting the value of ResultSet object
            metaData = resultSet.getMetaData();  // setting the value of ResultSetMetaData for reuse
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE RUNNING QUERY " + e.getMessage());
        }

        return resultSet;
    }

    /**
     * destroy method to clean up all the resources after being used
     */
    public static void close() {
        // WE HAVE TO CHECK IF WE HAVE THE VALID OBJECT FIRST BEFORE CLOSING THE RESOURCE
        // BECAUSE WE CAN NOT TAKE ACTION ON AN OBJECT THAT DOES NOT EXIST
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE CLOSING RESOURCES " + e.getMessage());
        }
    }

    /**
     * This method will reset the cursor to before first location
     */
    private static void resetCursor() {

        try {
            resultSet.beforeFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // find out the row count

    /**
     * find out the row count
     *
     * @return row count of this ResultSet
     */
    public static int getRowCount() {

        int rowCount = 0;
        try {
            resultSet.last();
            rowCount = resultSet.getRow();
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE GETTING ROW COUNT " + e.getMessage());
        } finally {
            resetCursor();
        }

        return rowCount;
    }

    /**
     * find out the column count
     *
     * @return column count of this ResultSet
     */
    public static int getColumnCount() {

        int columnCount = 0;

        try {
            columnCount = metaData.getColumnCount();

        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE GETTING COLUMN COUNT " + e.getMessage());
        }

        return columnCount;
    }

    /**
     * // Get all the Column names into a list object
     *
     * @return List<String>
     */
    public static List<String> getAllColumnNamesAsList() {

        List<String> columnNameLst = new ArrayList<>();

        try {
            for (int colIndex = 1; colIndex <= getColumnCount(); colIndex++) {
                String columnName = metaData.getColumnName(colIndex);
                columnNameLst.add(columnName);
            }
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getAllColumnNamesAsList " + e.getMessage());
        }

        return columnNameLst;
    }

    // get entire row of data according to row number

    /**
     * Getting entire row of data in a List of String
     *
     * @param rowNum row number to get as a list
     * @return row data as List of String
     */
    public static List<String> getRowDataAsList(int rowNum) {

        List<String> rowDataAsLst = new ArrayList<>();
        int colCount = getColumnCount();

        try {
            resultSet.absolute(rowNum);

            for (int colIndex = 1; colIndex <= colCount; colIndex++) {

                String cellValue = resultSet.getString(colIndex);
                rowDataAsLst.add(cellValue);

            }

        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getRowDataAsList " + e.getMessage());
        } finally {
            resetCursor();
        }

        return rowDataAsLst;
    }

    /**
     * getting the cell value according to row num and column index
     *
     * @param rowNum      row number to get the data from
     * @param columnIndex column number to get the data from
     * @return the value in String at that location
     */
    public static String getCellValue(int rowNum, int columnIndex) {

        String cellValue = "";

        try {
            resultSet.absolute(rowNum);
            cellValue = resultSet.getString(columnIndex);

        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getCellValue " + e.getMessage());
        } finally {
            resetCursor();
        }

        return cellValue;
    }

    /**
     * getting the cell value according to row num and column name
     *
     * @param rowNum     row number to get the data from
     * @param columnName column Name to get the data from
     * @return the value in String at that location
     */
    public static String getCellValue(int rowNum, String columnName) {

        String cellValue = "";

        try {
            resultSet.absolute(rowNum);
            cellValue = resultSet.getString(columnName);

        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getCellValue " + e.getMessage());
        } finally {
            resetCursor();
        }

        return cellValue;
    }

    /**
     * Get First Cell Value at First row First Column
     */
    public static String getFirstRowFirstColumn() {

        return getCellValue(1, 1);

    }

    /**
     * getting entire column data as list according to column number
     *
     * @param columnNum column number to get all data
     * @return List object that contains all rows of that column
     */
    public static List<String> getColumnDataAsList(int columnNum) {

        List<String> columnDataLst = new ArrayList<>();

        try {
            resultSet.beforeFirst(); // make sure the cursor is at before first location
            while (resultSet.next()) {

                String cellValue = resultSet.getString(columnNum);
                columnDataLst.add(cellValue);
            }

        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getColumnDataAsList " + e.getMessage());
        } finally {
            resetCursor();
        }

        return columnDataLst;
    }

    /**
     * getting entire column data as list according to column Name
     *
     * @param columnName column name to get all data
     * @return List object that contains all rows of that column
     */
    public static List<String> getColumnDataAsList(String columnName) {

        List<String> columnDataLst = new ArrayList<>();

        try {
            resultSet.beforeFirst(); // make sure the cursor is at before first location
            while (resultSet.next()) {

                String cellValue = resultSet.getString(columnName);
                columnDataLst.add(cellValue);
            }

        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getColumnDataAsList " + e.getMessage());
        } finally {
            resetCursor();
        }

        return columnDataLst;
    }


    /**
     * display all data from the ResultSet Object
     */
    public static void displayAllData() {

        int columnCount = getColumnCount();
        resetCursor();
        try {

            while (resultSet.next()) {

                for (int colIndex = 1; colIndex <= columnCount; colIndex++) {

                    //System.out.print( rs.getString(colIndex) + "\t" );
                    System.out.printf("%-25s", resultSet.getString(colIndex));
                }
                System.out.println();

            }

        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE displayAllData " + e.getMessage());
        } finally {
            resetCursor();
        }

    }

    /**
     * Save entire row data as Map<String,String>
     *
     * @param rowNum row number
     * @return Map object that contains key value pair
     * key     : column name
     * value   : cell value
     */
    public static Map<String, String> getRowMap(int rowNum) {

        Map<String, String> rowMap = new LinkedHashMap<>();
        int columnCount = getColumnCount();

        try {

            resultSet.absolute(rowNum);

            for (int colIndex = 1; colIndex <= columnCount; colIndex++) {
                String columnName = metaData.getColumnName(colIndex);
                String cellValue = resultSet.getString(colIndex);
                rowMap.put(columnName, cellValue);
            }

        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getRowMap " + e.getMessage());
        } finally {
            resetCursor();
        }


        return rowMap;
    }

    /**
     * We know how to store one row as map object
     * Now Store All rows as List of Map object
     *
     * @return List of Map object that contain each row data as Map<String,String>
     */
    public static List<Map<String, String>> getAllRowAsListOfMap() {

        List<Map<String, String>> allRowLstOfMap = new ArrayList<>();
        int rowCount = getRowCount();

        // move from first row till last row
        // get each row as map object and add it to the list

        for (int rowIndex = 1; rowIndex <= rowCount; rowIndex++) {

            Map<String, String> rowMap = getRowMap(rowIndex);
            allRowLstOfMap.add(rowMap);

        }
        resetCursor();

        return allRowLstOfMap;
    }

}
