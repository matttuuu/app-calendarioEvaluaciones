package com.example.calendarapp_idra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelperPsicopedagogia extends SQLiteOpenHelper {

    private static final String CREATE_EVENTS_TABLE_PSICO = "create table " + DBStructurePsicopedagogia.EVENT_TABLE_NAME_PSICO+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            +DBStructurePsicopedagogia.EVENT_PSICO+" TEXT, "+DBStructurePsicopedagogia.TIME_PSICO+" TEXT, "+DBStructurePsicopedagogia.DATE_PSICO+" TEXT, "+DBStructurePsicopedagogia.MONTH_PSICO+" TEXT, "+DBStructurePsicopedagogia.YEAR_PSICO+" TEXT)";
    private static final String DROP_EVENTS_TABLE_PSICO= "DROP TABLE IF EXISTS "+DBStructurePsicopedagogia.EVENT_TABLE_NAME_PSICO;


    public DBOpenHelperPsicopedagogia(@Nullable Context context) {
        super(context, DBStructurePsicopedagogia.DB_NAME_PSICO, null, DBStructurePsicopedagogia.DB_VERSION_PSICO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE_PSICO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EVENTS_TABLE_PSICO);
        onCreate(db);
    }

    public void SaveEvent(String event, String time, String date, String month, String year, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructurePsicopedagogia.EVENT_PSICO,event);
        contentValues.put(DBStructurePsicopedagogia.TIME_PSICO,time);
        contentValues.put(DBStructurePsicopedagogia.DATE_PSICO,date);
        contentValues.put(DBStructurePsicopedagogia.MONTH_PSICO,month);
        contentValues.put(DBStructurePsicopedagogia.YEAR_PSICO,year);
        database.insert(DBStructurePsicopedagogia.EVENT_TABLE_NAME_PSICO, null, contentValues);

    }

    public Cursor ReadEvents(String date, SQLiteDatabase database){
        String [] Projections = {DBStructurePsicopedagogia.EVENT_PSICO, DBStructurePsicopedagogia.TIME_PSICO, DBStructurePsicopedagogia.DATE_PSICO, DBStructurePsicopedagogia.MONTH_PSICO, DBStructurePsicopedagogia.YEAR_PSICO};
        String Selections = DBStructurePsicopedagogia.DATE_PSICO +"=?";
        String [] SelectionArgs = {date};

        return database.query(DBStructurePsicopedagogia.EVENT_TABLE_NAME_PSICO, Projections, Selections, SelectionArgs, null, null, null);
    }

    public Cursor ReadEventsperMonth(String month, String year, SQLiteDatabase database){
        String [] Projections = {DBStructurePsicopedagogia.EVENT_PSICO, DBStructurePsicopedagogia.TIME_PSICO, DBStructurePsicopedagogia.DATE_PSICO, DBStructurePsicopedagogia.MONTH_PSICO, DBStructurePsicopedagogia.YEAR_PSICO};
        String Selections = DBStructurePsicopedagogia.MONTH_PSICO +"=? and "+DBStructurePsicopedagogia.YEAR_PSICO+"=?";
        String [] SelectionArgs = {month, year};
        return database.query(DBStructurePsicopedagogia.EVENT_TABLE_NAME_PSICO, Projections, Selections, SelectionArgs, null, null, null);
    }

    public void deleteEvent(String event, String date, String time, SQLiteDatabase database)
    {
        String selection = DBStructurePsicopedagogia.EVENT_PSICO+"=? and "+DBStructurePsicopedagogia.DATE_PSICO+"=? and "+DBStructurePsicopedagogia.TIME_PSICO+"=?";
        String[] selectionArg = {event, date, time};
        database.delete(DBStructurePsicopedagogia.EVENT_TABLE_NAME_PSICO, selection, selectionArg);
    }
    

}
