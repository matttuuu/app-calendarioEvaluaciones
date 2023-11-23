package com.example.calendarapp_idra;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CustomCalendarViewSeguridad extends LinearLayout {
    ImageButton nextButtonSeg, previousButtonSeg;
    TextView currentDateSeg;
    GridView gridViewSeg;

    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendarSeg = Calendar.getInstance(Locale.ENGLISH);
    Context contextSeg;
    SimpleDateFormat dateFormatSeg = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormatSeg = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormatSeg = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormateSeg = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);



    MyGridAdapterSeguridad myGridAdapterSeg;
    AlertDialog alertDialogSeg;
    List<Date> datesSeg = new ArrayList<>();
    List<EventsSeguridad> eventsListSeg = new ArrayList<>();

    public CustomCalendarViewSeguridad(Context context) {
        super(context);
    }

    public CustomCalendarViewSeguridad(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.contextSeg = context;
        InitializeLayout();
        SetUpCalendar();
        previousButtonSeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarSeg.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        nextButtonSeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarSeg.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridViewSeg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @SuppressLint("MissingInflatedId")
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout_seg, null);
                EditText EventName = addView.findViewById(R.id.eventnameSeg);
                TextView EventTime = addView.findViewById(R.id.eventtimeSeg);
                ImageButton SetTime = addView.findViewById(R.id.seteventtimeSeg);
                Button AddEvent = addView.findViewById(R.id.addeventSeg);
                SetTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minuts = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        Calendar c = Calendar.getInstance();
                                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        c.set(Calendar.MINUTE, minute);
                                        c.setTimeZone(TimeZone.getDefault());
                                        SimpleDateFormat hformate = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                        String event_Time = hformate.format(c.getTime());
                                        EventTime.setText(event_Time);
                                    }
                                },hours, minuts, false);
                        timePickerDialog.show();

                    }
                });
                final String date = eventDateFormateSeg.format(datesSeg.get(position));
                final String month = monthFormatSeg.format(datesSeg.get(position));
                final String year = yearFormatSeg.format(datesSeg.get(position));

                AddEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventName.getText().toString(),EventTime.getText().toString(), date, month, year);
                        SetUpCalendar();
                        alertDialogSeg.dismiss();
                    }
                });

                builder.setView(addView);
                alertDialogSeg = builder.create();
                alertDialogSeg.show();
                alertDialogSeg.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        SetUpCalendar();
                    }
                });

            }
        });

        gridViewSeg.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("MissingInflatedId") //AGREGADO POR MI, EN CASO DE QUE NO ANDE, BORRAR Y BUSCAR OTRA SOLUCIÓN.
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String dateSeg = eventDateFormateSeg.format(datesSeg.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout_seg, null);
                RecyclerView recyclerView = showView.findViewById(R.id.EventsRVSeg);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapterSeguridad eventRecyclerAdapterSeguridad = new EventRecyclerAdapterSeguridad(showView.getContext(),
                        CollectEventByDate(dateSeg));
                recyclerView.setAdapter(eventRecyclerAdapterSeguridad);
                eventRecyclerAdapterSeguridad.notifyDataSetChanged();

                builder.setView(showView);
                alertDialogSeg = builder.create();
                alertDialogSeg.show();


                return true;
            }
        });




    }

    private ArrayList<EventsSeguridad> CollectEventByDate(String date) {
        ArrayList<EventsSeguridad> arrayList = new ArrayList<>();
        DBOpenHelperSeguridad dbOpenHelper = new DBOpenHelperSeguridad(contextSeg); //DBOPENHELPER TAMBIÉN PUESTO, NO SÉ SI ESTA BIEN.
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String eventSeg = cursor.getString(cursor.getColumnIndex(DBStructureSeguridad.EVENT_SEG)); //LOS SUPRESSLINT SON INVENCIÓN MÍA, EN CASO DE ERROR AL USAR ESTE MÉTODO, BORRALO BEBEEE
            @SuppressLint("Range") String timeSeg = cursor.getString(cursor.getColumnIndex(DBStructureSeguridad.TIME_SEG));
            @SuppressLint("Range") String DateSeg = cursor.getString(cursor.getColumnIndex(DBStructureSeguridad.DATE_SEG));
            @SuppressLint("Range") String monthSeg = cursor.getString(cursor.getColumnIndex(DBStructureSeguridad.MONTH_SEG));
            @SuppressLint("Range") String YearSeg = cursor.getString(cursor.getColumnIndex(DBStructureSeguridad.YEAR_SEG));
            EventsSeguridad eventsSeguridad = new EventsSeguridad(eventSeg, timeSeg, DateSeg, monthSeg, YearSeg);
            arrayList.add(eventsSeguridad);

        }
        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }



    public CustomCalendarViewSeguridad(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void SaveEvent (String event, String time, String date, String month, String year){


        DBOpenHelperSeguridad dbOpenHelper = new DBOpenHelperSeguridad(contextSeg); // SI NO, NO ANDA LA VARIABLE dbOpenHelper, revisar despues.
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, time, date, month, year, database);
        dbOpenHelper.close();
        Toast.makeText(contextSeg, "Event Saved", Toast.LENGTH_SHORT).show();


    }
    @SuppressLint("MissingInflatedId")
    private void InitializeLayout(){
        LayoutInflater inflater = (LayoutInflater)contextSeg.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout_seg, this);
        nextButtonSeg = view.findViewById(R.id.nextBtnSeg);
        previousButtonSeg = view.findViewById(R.id.previousBtnSeg);
        currentDateSeg = view.findViewById(R.id.current_DateSeg);
        gridViewSeg = view.findViewById(R.id.gridviewSeg);

    }
    private void SetUpCalendar(){
        String currwntDateSeg = dateFormatSeg.format(calendarSeg.getTime()); //EL NOMBRE DE LA VARIABLE ES RARO, SÍ.  PERO EL VIDEO QUE SEGUÍ LA PONE ASÍ.
        currentDateSeg.setText(currwntDateSeg);

        datesSeg.clear();
        Calendar monthCalendar = (Calendar) calendarSeg.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEventsPerMonth(monthFormatSeg.format(calendarSeg.getTime()), yearFormatSeg.format(calendarSeg.getTime()));

        while (datesSeg.size() < MAX_CALENDAR_DAYS) {
            datesSeg.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        myGridAdapterSeg = new MyGridAdapterSeguridad(contextSeg, datesSeg, calendarSeg, eventsListSeg);
        gridViewSeg.setAdapter(myGridAdapterSeg);


    }

    private void CollectEventsPerMonth(String Month, String year){


        eventsListSeg.clear();
        DBOpenHelperSeguridad dbOpenHelperSeguridad = new DBOpenHelperSeguridad(contextSeg); //REVISAR DESPUÉS PROBLEMA CON DB
        SQLiteDatabase database = dbOpenHelperSeguridad.getReadableDatabase();
        Cursor cursor = dbOpenHelperSeguridad.ReadEventsperMonth(Month, year, database);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String event = cursor.getString(cursor.getColumnIndex(DBStructureSeguridad.EVENT_SEG)); //LOS SUPRESSLINT SON INVENCIÓN MÍA, EN CASO DE ERROR AL USAR ESTE MÉTODO, BORRALO BEBEEE
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DBStructureSeguridad.TIME_SEG));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DBStructureSeguridad.DATE_SEG));
            @SuppressLint("Range") String month = cursor.getString(cursor.getColumnIndex(DBStructureSeguridad.MONTH_SEG));
            @SuppressLint("Range") String Year = cursor.getString(cursor.getColumnIndex(DBStructureSeguridad.YEAR_SEG));
            EventsSeguridad eventsSeguridad = new EventsSeguridad(event, time, date, month, Year);
            eventsListSeg.add(eventsSeguridad);

        }
        cursor.close();
        dbOpenHelperSeguridad.close();

    }

}
