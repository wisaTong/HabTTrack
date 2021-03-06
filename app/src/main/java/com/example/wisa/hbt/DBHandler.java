package com.example.wisa.hbt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.sql.SQLData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;

public class DBHandler extends SQLiteOpenHelper {

    // increment every time changes is made to the database structure
    private static final int DATABASE_VERSION = 17;

    private static final String DATABASE_NAME = "HBTracker.db";

    // Activity table
    public static final String TABLE_ACTIVITY = "activity";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_START = "startDate";

    // Tracker table
    public static final String TABLE_TRACKER = "tracker";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ACTIVITY = "activity_id";

    public static final String DATE_FOMAT = "yyyy/MM/dd";

    /** Constructor */
    public DBHandler (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TURN ON CASCADING DELETES
        String query = "PRAGMA foreign_keys = ON";
        db.execSQL(query);

        // TABLE_ACTIVITY
        query = "CREATE TABLE " + TABLE_ACTIVITY + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT UNIQUE, " +
                COLUMN_START + " TEXT);";
        db.execSQL(query);

        // TABLE_TRACKER
        query = "CREATE TABLE " + TABLE_TRACKER + "(" +
                COLUMN_DATE + " TEXT, " +
                COLUMN_ACTIVITY + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ACTIVITY + ") REFERENCES " + TABLE_ACTIVITY +
                "(" + COLUMN_ID + ") ON DELETE CASCADE, " + "UNIQUE(" + COLUMN_DATE + ", " + COLUMN_ACTIVITY + "));";
        db.execSQL(query);

//        //add Sample
//        String[] date = {
//                "2018/05/05", "2018/05/06", "2018/05/07", "2018/05/08", "2018/05/09", "2018/05/10", "2018/05/11"
//        };
//        addSample(date, date[0], "Sample1");
//        addSample(date, date[0], "Sample2");
//        addSample(date, date[0], "Sample3");

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON");
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
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat(DATE_FOMAT);

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_START, format.format(today));
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
        int id = getActivityID(name);
        if (id <= 0) return;;
        String query = "DELETE FROM " + TABLE_ACTIVITY + " WHERE " +
                 COLUMN_ID + " = " + id;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
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
        int id = getActivityID(name);
        String query = "SELECT * FROM " + TABLE_TRACKER + " WHERE " +
                COLUMN_ACTIVITY + " = " + id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query,null);

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

    /**
     * Search for activity id in TABLE_ACTIVITY
     * @param name String name of activity
     * @return int value of id
     */
    public int getActivityID(String name) {
        String query = "SELECT * FROM " + TABLE_ACTIVITY + " WHERE " + COLUMN_NAME + " = \"" + name + "\"" ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if (c.getCount() <= 0) return 0;
        int id = Integer.parseInt(c.getString(c.getColumnIndex(COLUMN_ID)));
        c.close();
        return id;
    }

    /**
     * @return Date object of the first record in COLUMN_DATE of TABLE_TRACKER
     */
    public Date getEarliestDate() {
        String query = "SELECT * FROM " + TABLE_ACTIVITY + " LIMIT 1";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        SimpleDateFormat format = new SimpleDateFormat(DATE_FOMAT);
        try {
            return format.parse(c.getString(c.getColumnIndex(COLUMN_START)));
        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * Query and check whether given activity has been
     * recorded as done today
     * @param activity is String value of activity name
     * @return true if found.
     *         false if not.
     */
    public boolean isDoneToday(String activity) {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        SimpleDateFormat formatter  = new SimpleDateFormat(DATE_FOMAT);
        String date = formatter.format(today);

        String query = "SELECT " + COLUMN_DATE + ", " + COLUMN_ACTIVITY + " FROM " + TABLE_TRACKER +
                " WHERE " + COLUMN_DATE + " = " + "\"" + date + "\" AND " +
                COLUMN_ACTIVITY + " = " + getActivityID(activity);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() <= 0) return false;
        db.close();
        c.close();
        return true;
    }

    /**
     * @param activity is a given activity_id to query
     * @return number of record of given activity.
     */
    public int howManyDone(String activity) {
        String query = "SELECT * FROM " + TABLE_TRACKER + " WHERE " +
                COLUMN_ACTIVITY + " = " + getActivityID(activity);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int count = c.getCount();
        db.close();
        c.close();
        return count;
    }

    /**
     * Query, parse, and return Date object of start date
     * @param activity is String name of activity
     * @return start Date
     */
    public Date getStartDate(String activity) {
        String query = "SELECT " + COLUMN_START + " FROM " + TABLE_ACTIVITY + " WHERE " +
                COLUMN_NAME + " = " + "\"" +activity + "\"";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String date = c.getString(c.getColumnIndex(COLUMN_START));
        SimpleDateFormat format = new SimpleDateFormat(DATE_FOMAT);
        Date start = new Date();
        try {
            start = format.parse(date);
        } catch (ParseException ex) {
            //TODO HANDLE PARSE EXCEPTION
        } return start;
    }

    /**
     * Query for Text of start date
     * @param activity is String name of activity
     * @return String of start date in DATE_FORMAT format
     */
    public String startDateString(String activity) {
        String query = "SELECT " + COLUMN_START + " FROM " + TABLE_ACTIVITY + " WHERE " +
                COLUMN_NAME + " = " + "\"" +activity + "\"";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String date = c.getString(c.getColumnIndex(COLUMN_START));
        return date;
    }

    /**
     * @param activity String value of activity name
     * @return day count from the dat activity is added
     */
    public int daysFromStart(String activity) {
        Date start = getStartDate(activity);
        Calendar cal = Calendar.getInstance();
        return Days.daysBetween(new DateTime(start), new DateTime(cal.getTime())).getDays() + 1;
    }

    /**
     * Add smaples data to database for debugging
     * @deprecated do not use please.
     */
    @Deprecated
    public void addSample(String[] dates, String start, String name){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_START, start);
        db.insert(TABLE_ACTIVITY, null, values);

        for (String date : dates) {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FOMAT);
            try {
                addDateCheck(format.parse(date), getActivityID(name));
            } catch (Exception ex) {

            }
        }
    }
}
