package com.example.calendarapp_idra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelperSeguridad extends SQLiteOpenHelper {

    private static final String CREATE_EVENTS_TABLE_SEG = "create table " + DBStructureSeguridad.EVENT_TABLE_NAME_SEG+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            +DBStructureSeguridad.EVENT_SEG+" TEXT, "+DBStructureSeguridad.TIME_SEG+" TEXT, "+DBStructureSeguridad.DATE_SEG+" TEXT, "+DBStructureSeguridad.MONTH_SEG+" TEXT, "+DBStructureSeguridad.YEAR_SEG+" TEXT)";
    private static final String DROP_EVENTS_TABLE_SEG= "DROP TABLE IF EXISTS "+DBStructureSeguridad.EVENT_TABLE_NAME_SEG;


    public DBOpenHelperSeguridad(@Nullable Context context) {
        super(context, DBStructureSeguridad.DB_NAME_SEG, null, DBStructureSeguridad.DB_VERSION_SEG);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE_SEG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EVENTS_TABLE_SEG);
        onCreate(db);
    }

    public void SaveEvent(String event, String time, String date, String month, String year, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructureSeguridad.EVENT_SEG,event);
        contentValues.put(DBStructureSeguridad.TIME_SEG,time);
        contentValues.put(DBStructureSeguridad.DATE_SEG,date);
        contentValues.put(DBStructureSeguridad.MONTH_SEG,month);
        contentValues.put(DBStructureSeguridad.YEAR_SEG,year);
        database.insert(DBStructureSeguridad.EVENT_TABLE_NAME_SEG, null, contentValues);

    }

    public Cursor ReadEvents(String date, SQLiteDatabase database){
        String [] Projections = {DBStructureSeguridad.EVENT_SEG, DBStructureSeguridad.TIME_SEG, DBStructureSeguridad.DATE_SEG, DBStructureSeguridad.MONTH_SEG, DBStructureSeguridad.YEAR_SEG};
        String Selections = DBStructureSeguridad.DATE_SEG +"=?";
        String [] SelectionArgs = {date};

        return database.query(DBStructureSeguridad.EVENT_TABLE_NAME_SEG, Projections, Selections, SelectionArgs, null, null, null);
    }

    public Cursor ReadEventsperMonth(String month, String year, SQLiteDatabase database){
        String [] Projections = {DBStructureSeguridad.EVENT_SEG, DBStructureSeguridad.TIME_SEG, DBStructureSeguridad.DATE_SEG, DBStructureSeguridad.MONTH_SEG, DBStructureSeguridad.YEAR_SEG};
        String Selections = DBStructureSeguridad.MONTH_SEG +"=? and "+DBStructureSeguridad.YEAR_SEG+"=?";
        String [] SelectionArgs = {month, year};
        return database.query(DBStructureSeguridad.EVENT_TABLE_NAME_SEG, Projections, Selections, SelectionArgs, null, null, null);
    }

    public void deleteEvent(String event, String date, String time, SQLiteDatabase database)
    {
        String selection = DBStructureSeguridad.EVENT_SEG+"=? and "+DBStructureSeguridad.DATE_SEG+"=? and "+DBStructureSeguridad.TIME_SEG+"=?";
        String[] selectionArg = {event, date, time};
        database.delete(DBStructureSeguridad.EVENT_TABLE_NAME_SEG, selection, selectionArg);
    }





}
