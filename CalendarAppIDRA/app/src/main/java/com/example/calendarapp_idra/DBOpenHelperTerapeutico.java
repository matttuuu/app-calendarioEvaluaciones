package com.example.calendarapp_idra;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;


public class DBOpenHelperTerapeutico extends SQLiteOpenHelper {

    private static final String CREATE_EVENTS_TABLE_TERA = "create table " + DBStructureTerapeutico.EVENT_TABLE_NAME_TERA+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            +DBStructureTerapeutico.EVENT_TERA+" TEXT, "+DBStructureTerapeutico.TIME_TERA+" TEXT, "+DBStructureTerapeutico.DATE_TERA+" TEXT, "+DBStructureTerapeutico.MONTH_TERA+" TEXT, "+DBStructureTerapeutico.YEAR_TERA+" TEXT)";
    private static final String DROP_EVENTS_TABLE_TERA= "DROP TABLE IF EXISTS "+DBStructureTerapeutico.EVENT_TABLE_NAME_TERA;


    public DBOpenHelperTerapeutico(@Nullable Context context) {
        super(context, DBStructureTerapeutico.DB_NAME_TERA, null, DBStructureTerapeutico.DB_VERSION_TERA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE_TERA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EVENTS_TABLE_TERA);
        onCreate(db);
    }

    public void SaveEvent(String event, String time, String date, String month, String year, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructureTerapeutico.EVENT_TERA,event);
        contentValues.put(DBStructureTerapeutico.TIME_TERA,time);
        contentValues.put(DBStructureTerapeutico.DATE_TERA,date);
        contentValues.put(DBStructureTerapeutico.MONTH_TERA,month);
        contentValues.put(DBStructureTerapeutico.YEAR_TERA,year);
        database.insert(DBStructureTerapeutico.EVENT_TABLE_NAME_TERA, null, contentValues);

    }

    public Cursor ReadEvents(String date, SQLiteDatabase database){
        String [] Projections = {DBStructureTerapeutico.EVENT_TERA, DBStructureTerapeutico.TIME_TERA, DBStructureTerapeutico.DATE_TERA, DBStructureTerapeutico.MONTH_TERA, DBStructureTerapeutico.YEAR_TERA};
        String Selections = DBStructureTerapeutico.DATE_TERA +"=?";
        String [] SelectionArgs = {date};

        return database.query(DBStructureTerapeutico.EVENT_TABLE_NAME_TERA, Projections, Selections, SelectionArgs, null, null, null);
    }

    public Cursor ReadEventsperMonth(String month, String year, SQLiteDatabase database){
        String [] Projections = {DBStructureTerapeutico.EVENT_TERA, DBStructureTerapeutico.TIME_TERA, DBStructureTerapeutico.DATE_TERA, DBStructureTerapeutico.MONTH_TERA, DBStructureTerapeutico.YEAR_TERA};
        String Selections = DBStructureTerapeutico.MONTH_TERA +"=? and "+DBStructureTerapeutico.YEAR_TERA+"=?";
        String [] SelectionArgs = {month, year};
        return database.query(DBStructureTerapeutico.EVENT_TABLE_NAME_TERA, Projections, Selections, SelectionArgs, null, null, null);
    }

    public void deleteEvent(String event, String date, String time, SQLiteDatabase database)
    {
        String selection = DBStructureTerapeutico.EVENT_TERA+"=? and "+DBStructureTerapeutico.DATE_TERA+"=? and "+DBStructureTerapeutico.TIME_TERA+"=?";
        String[] selectionArg = {event, date, time};
        database.delete(DBStructureTerapeutico.EVENT_TABLE_NAME_TERA, selection, selectionArg);
    }





}
