package com.example.wisa.hbt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // increment everytime changes is made to the database
    private static final int DATABASE_VERSION = 9;

    private static final String DATABASE_NAME = "HBTracker.db";

    // Activity table
    public static final String TABLE_ACTIVITY = "activity";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";

    // Tracker table
    public static final String TABLE_TRACKER = "tracker";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ACTIVITY = "activity";

    public static final String DATE_FOMAT = "yyyy/MM/dd";

    /** Constructer */
    public DBHandler (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TABLE_ACTIVITY
        String query = "CREATE TABLE " + TABLE_ACTIVITY + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT UNIQUE);";
        db.execSQL(query);

        // TABLE_TRACKER
        query = "CREATE TABLE " + TABLE_TRACKER + "(" +
                COLUMN_DATE + " TEXT, " +
                COLUMN_ACTIVITY + " INTEGER," +
                "FOREIGN KEY(" + COLUMN_ACTIVITY + ") REFERENCES " + TABLE_ACTIVITY +
                "(" + COLUMN_ID + "), " + "UNIQUE(" + COLUMN_DATE + ", " + COLUMN_ACTIVITY + "));";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
        onCreate(db);
    }

    /**
     * Add new activity row to TABLE_ACTIVITY
     * @param name is String name describing given activity
     */
    public void addActivity(String name) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ACTIVITY, null, values);
        db.close();
    }

    /**
     * Add new row with date and activity to TABLE_TRACKER
     * @param date is Date object get from local device
     * @param activityID is ID for activity
     */
    public void addDateCheck(Date date , int activityID){
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FOMAT);
        String format = formatter.format(date);

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, format);
        values.put(COLUMN_ACTIVITY, activityID);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TRACKER, null, values);
        db.close();
    }

    /**
     * Delete a certain activity from database by given name, from COLUMN_NAME
     * @param name is name String of activity wanted to delete
     */
    public void deleteActivity(String name) {
        String query = "DELETE FROM " + TABLE_ACTIVITY + " WHERE " +
                COLUMN_NAME + " LIKE " + "\"" + name + "\"";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }

    /**
     * read all data from TABLE_TRACKER and return as String
     * @return String value of all data in TABLE_ACTIVITY
     */
    public String recordToString() {
        //TODO MODIFY THIS LATER
        String query = "SELECT * FROM " + TABLE_TRACKER;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        String data = "";
        if (c.moveToFirst()) {
            do {
                data += c.getString(c.getColumnIndex(COLUMN_DATE));
                data += ("     " + c.getString(c.getColumnIndex(COLUMN_ACTIVITY)) + "\n");
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return data;
    }

    /**
     * Query and get a list of String of activities in TABLE_ACTIVITY
     * @return List of activities name
     */
    public List<String> getActivities() {
        String query = "SELECT * FROM " + TABLE_ACTIVITY;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        List<String> activities = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                String activity = c.getString(c.getColumnIndex(COLUMN_NAME));
                activities.add(activity);
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return activities;
    }

    /**
     * Get a List of date that certain activity is marked as done
     * @param name is String name of activity
     * @return List<Date>
     */
    public List<Date> getDateDone(String name) {
        String query = "SELECT * FROM " + TABLE_ACTIVITY + " WHERE " + COLUMN_NAME + " = \"" + name + "\"" ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        int id = c.getInt(c.getColumnIndex(COLUMN_ID));
        query = "SELECT * FROM " + TABLE_TRACKER + " WHERE " +
                COLUMN_ACTIVITY + " = " + id;
        c = db.rawQuery(query,null);

        List<Date> dateList = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                String date = c.getString(c.getColumnIndex(COLUMN_DATE));
                SimpleDateFormat formatter = new SimpleDateFormat(DATE_FOMAT);
                try {
                    Date parsed = formatter.parse(date);
                    dateList.add(parsed);
                } catch (ParseException ex) {
                    //TODO HANDLE PARSE EXCEPTION
                }
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return dateList;
    }

}