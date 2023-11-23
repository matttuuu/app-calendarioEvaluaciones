package com.example.calendarapp_idra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelperIngles extends SQLiteOpenHelper {

    private static final String CREATE_EVENTS_TABLE_ING = "create table " + DBStructureIngles.EVENT_TABLE_NAME_ING+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            +DBStructureIngles.EVENT_ING+" TEXT, "+DBStructureIngles.TIME_ING+" TEXT, "+DBStructureIngles.DATE_ING+" TEXT, "+DBStructureIngles.MONTH_ING+" TEXT, "+DBStructureIngles.YEAR_ING+" TEXT)";
    private static final String DROP_EVENTS_TABLE_ING= "DROP TABLE IF EXISTS "+DBStructureIngles.EVENT_TABLE_NAME_ING;


    public DBOpenHelperIngles(@Nullable Context context) {
        super(context, DBStructureIngles.DB_NAME_ING, null, DBStructureIngles.DB_VERSION_ING);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE_ING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EVENTS_TABLE_ING);
        onCreate(db);
    }

    public void SaveEvent(String event, String time, String date, String month, String year, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructureIngles.EVENT_ING,event);
        contentValues.put(DBStructureIngles.TIME_ING,time);
        contentValues.put(DBStructureIngles.DATE_ING,date);
        contentValues.put(DBStructureIngles.MONTH_ING,month);
        contentValues.put(DBStructureIngles.YEAR_ING,year);
        database.insert(DBStructureIngles.EVENT_TABLE_NAME_ING, null, contentValues);

    }

    public Cursor ReadEvents(String date, SQLiteDatabase database){
        String [] Projections = {DBStructureIngles.EVENT_ING, DBStructureIngles.TIME_ING, DBStructureIngles.DATE_ING, DBStructureIngles.MONTH_ING, DBStructureIngles.YEAR_ING};
        String Selections = DBStructureIngles.DATE_ING +"=?";
        String [] SelectionArgs = {date};

        return database.query(DBStructureIngles.EVENT_TABLE_NAME_ING, Projections, Selections, SelectionArgs, null, null, null);
    }

    public Cursor ReadEventsperMonth(String month, String year, SQLiteDatabase database){
        String [] Projections = {DBStructureIngles.EVENT_ING, DBStructureIngles.TIME_ING, DBStructureIngles.DATE_ING, DBStructureIngles.MONTH_ING, DBStructureIngles.YEAR_ING};
        String Selections = DBStructureIngles.MONTH_ING +"=? and "+DBStructureIngles.YEAR_ING+"=?";
        String [] SelectionArgs = {month, year};
        return database.query(DBStructureIngles.EVENT_TABLE_NAME_ING, Projections, Selections, SelectionArgs, null, null, null);
    }

    public void deleteEvent(String event, String date, String time, SQLiteDatabase database)
    {
        String selection = DBStructureIngles.EVENT_ING+"=? and "+DBStructureIngles.DATE_ING+"=? and "+DBStructureIngles.TIME_ING+"=?";
        String[] selectionArg = {event, date, time};
        database.delete(DBStructureIngles.EVENT_TABLE_NAME_ING, selection, selectionArg);
    }
}
