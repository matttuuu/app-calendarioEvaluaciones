package Class_Ingles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;



public class DBOpenHelperIngles extends SQLiteOpenHelper {

    private static final String CREATE_EVENTS_TABLE_INGLES = "create table " + DBStructureIngles.EVENT_TABLE_NAME_INGLES+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            +DBStructureIngles.EVENT_INGLES+" TEXT, "+DBStructureIngles.TIME_INGLES+" TEXT, "+DBStructureIngles.DATE_INGLES+" TEXT, "+DBStructureIngles.MONTH_INGLES+" TEXT, "+DBStructureIngles.YEAR_INGLES+" TEXT)";
    private static final String DROP_EVENTS_TABLE_INGLES= "DROP TABLE IF EXISTS "+DBStructureIngles.EVENT_TABLE_NAME_INGLES;


    public DBOpenHelperIngles(@Nullable Context context) {
        super(context, DBStructureIngles.DB_NAME_INGLES, null, DBStructureIngles.DB_VERSION_INGLES);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE_INGLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EVENTS_TABLE_INGLES);
        onCreate(db);
    }

    public void SaveEvent(String event, String time, String date, String month, String year, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructureIngles.EVENT_INGLES,event);
        contentValues.put(DBStructureIngles.TIME_INGLES,time);
        contentValues.put(DBStructureIngles.DATE_INGLES,date);
        contentValues.put(DBStructureIngles.MONTH_INGLES,month);
        contentValues.put(DBStructureIngles.YEAR_INGLES,year);
        database.insert(DBStructureIngles.EVENT_TABLE_NAME_INGLES, null, contentValues);

    }

    public Cursor ReadEvents(String date, SQLiteDatabase database){
        String [] Projections = {DBStructureIngles.EVENT_INGLES, DBStructureIngles.TIME_INGLES, DBStructureIngles.DATE_INGLES, DBStructureIngles.MONTH_INGLES, DBStructureIngles.YEAR_INGLES};
        String Selections = DBStructureIngles.DATE_INGLES +"=?";
        String [] SelectionArgs = {date};

        return database.query(DBStructureIngles.EVENT_TABLE_NAME_INGLES, Projections, Selections, SelectionArgs, null, null, null);
    }

    public Cursor ReadEventsperMonth(String month, String year, SQLiteDatabase database){
        String [] Projections = {DBStructureIngles.EVENT_INGLES, DBStructureIngles.TIME_INGLES, DBStructureIngles.DATE_INGLES, DBStructureIngles.MONTH_INGLES, DBStructureIngles.YEAR_INGLES};
        String Selections = DBStructureIngles.MONTH_INGLES +"=? and "+DBStructureIngles.YEAR_INGLES+"=?";
        String [] SelectionArgs = {month, year};
        return database.query(DBStructureIngles.EVENT_TABLE_NAME_INGLES, Projections, Selections, SelectionArgs, null, null, null);
    }

    public void deleteEvent(String event, String date, String time, SQLiteDatabase database)
    {
        String selection = DBStructureIngles.EVENT_INGLES+"=? and "+DBStructureIngles.DATE_INGLES+"=? and "+DBStructureIngles.TIME_INGLES+"=?";
        String[] selectionArg = {event, date, time};
        database.delete(DBStructureIngles.EVENT_TABLE_NAME_INGLES, selection, selectionArg);
    }

}
