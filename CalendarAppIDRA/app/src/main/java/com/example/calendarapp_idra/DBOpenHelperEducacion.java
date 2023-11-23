package com.example.calendarapp_idra;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class DBOpenHelperEducacion extends SQLiteOpenHelper{

    private static final String CREATE_EVENTS_TABLE_EDU = "create table " + DBStructureEducacion.EVENT_TABLE_NAME_EDU+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            +DBStructureEducacion.EVENT_EDU+" TEXT, "+DBStructureEducacion.TIME_EDU+" TEXT, "+DBStructureEducacion.DATE_EDU+" TEXT, "+DBStructureEducacion.MONTH_EDU+" TEXT, "+DBStructureEducacion.YEAR_EDU+" TEXT)";
    private static final String DROP_EVENTS_TABLE_EDU= "DROP TABLE IF EXISTS "+DBStructureEducacion.EVENT_TABLE_NAME_EDU;


    public DBOpenHelperEducacion(@Nullable Context context) {
        super(context, DBStructureEducacion.DB_NAME_EDU, null, DBStructureEducacion.DB_VERSION_EDU);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE_EDU);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EVENTS_TABLE_EDU);
        onCreate(db);
    }

    public void SaveEvent(String event, String time, String date, String month, String year, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructureEducacion.EVENT_EDU,event);
        contentValues.put(DBStructureEducacion.TIME_EDU,time);
        contentValues.put(DBStructureEducacion.DATE_EDU,date);
        contentValues.put(DBStructureEducacion.MONTH_EDU,month);
        contentValues.put(DBStructureEducacion.YEAR_EDU,year);
        database.insert(DBStructureEducacion.EVENT_TABLE_NAME_EDU, null, contentValues);

    }

    public Cursor ReadEvents(String date, SQLiteDatabase database){
        String [] Projections = {DBStructureEducacion.EVENT_EDU, DBStructureEducacion.TIME_EDU, DBStructureEducacion.DATE_EDU, DBStructureEducacion.MONTH_EDU, DBStructureEducacion.YEAR_EDU};
        String Selections = DBStructureEducacion.DATE_EDU +"=?";
        String [] SelectionArgs = {date};

        return database.query(DBStructureEducacion.EVENT_TABLE_NAME_EDU, Projections, Selections, SelectionArgs, null, null, null);
    }

    public Cursor ReadEventsperMonth(String month, String year, SQLiteDatabase database){
        String [] Projections = {DBStructureEducacion.EVENT_EDU, DBStructureEducacion.TIME_EDU, DBStructureEducacion.DATE_EDU, DBStructureEducacion.MONTH_EDU, DBStructureEducacion.YEAR_EDU};
        String Selections = DBStructureEducacion.MONTH_EDU +"=? and "+DBStructureEducacion.YEAR_EDU+"=?";
        String [] SelectionArgs = {month, year};
        return database.query(DBStructureEducacion.EVENT_TABLE_NAME_EDU, Projections, Selections, SelectionArgs, null, null, null);
    }

    public void deleteEvent(String event, String date, String time, SQLiteDatabase database)
    {
        String selection = DBStructureEducacion.EVENT_EDU+"=? and "+DBStructureEducacion.DATE_EDU+"=? and "+DBStructureEducacion.TIME_EDU+"=?";
        String[] selectionArg = {event, date, time};
        database.delete(DBStructureEducacion.EVENT_TABLE_NAME_EDU, selection, selectionArg);
    }

    


}
