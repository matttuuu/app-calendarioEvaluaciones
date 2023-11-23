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

public class CustomCalendarViewPsicopedagogia extends LinearLayout {
    ImageButton nextButtonPsico, previousButtonPsico;
    TextView currentDatePsico;
    GridView gridViewPsico;

    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendarPsico = Calendar.getInstance(Locale.ENGLISH);
    Context contextPsico;
    SimpleDateFormat dateFormatPsico = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormatPsico = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormatPsico = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormatePsico = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);



    MyGridAdapterPsicopedagogia myGridAdapterPsico;
    AlertDialog alertDialogPsico;
    List<Date> datesPsico = new ArrayList<>();
    List<EventsPsicopedagogia> eventsListPsico = new ArrayList<>();

    public CustomCalendarViewPsicopedagogia(Context context) {
        super(context);
    }

    public CustomCalendarViewPsicopedagogia(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.contextPsico = context;
        InitializeLayout();
        SetUpCalendar();
        previousButtonPsico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPsico.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        nextButtonPsico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPsico.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridViewPsico.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @SuppressLint("MissingInflatedId")
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout_psico, null);
                EditText EventName = addView.findViewById(R.id.eventnamePsico);
                TextView EventTime = addView.findViewById(R.id.eventtimePsico);
                ImageButton SetTime = addView.findViewById(R.id.seteventtimePsico);
                Button AddEvent = addView.findViewById(R.id.addeventPsico);
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
                final String date = eventDateFormatePsico.format(datesPsico.get(position));
                final String month = monthFormatPsico.format(datesPsico.get(position));
                final String year = yearFormatPsico.format(datesPsico.get(position));

                AddEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventName.getText().toString(),EventTime.getText().toString(), date, month, year);
                        SetUpCalendar();
                        alertDialogPsico.dismiss();
                    }
                });

                builder.setView(addView);
                alertDialogPsico = builder.create();
                alertDialogPsico.show();
                alertDialogPsico.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        SetUpCalendar();
                    }
                });

            }
        });

        gridViewPsico.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("MissingInflatedId") //AGREGADO POR MI, EN CASO DE QUE NO ANDE, BORRAR Y BUSCAR OTRA SOLUCIÓN.
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String datePsico = eventDateFormatePsico.format(datesPsico.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout_psico, null);
                RecyclerView recyclerView = showView.findViewById(R.id.EventsRVPsico);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapterPsicopedagogia eventRecyclerAdapterPsicopedagogia = new EventRecyclerAdapterPsicopedagogia(showView.getContext(),
                        CollectEventByDate(datePsico));
                recyclerView.setAdapter(eventRecyclerAdapterPsicopedagogia);
                eventRecyclerAdapterPsicopedagogia.notifyDataSetChanged();

                builder.setView(showView);
                alertDialogPsico = builder.create();
                alertDialogPsico.show();


                return true;
            }
        });




    }

    private ArrayList<EventsPsicopedagogia> CollectEventByDate(String date) {
        ArrayList<EventsPsicopedagogia> arrayList = new ArrayList<>();
        DBOpenHelperPsicopedagogia dbOpenHelper = new DBOpenHelperPsicopedagogia(contextPsico); //DBOPENHELPER TAMBIÉN PUESTO, NO SÉ SI ESTA BIEN.
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String eventPsico = cursor.getString(cursor.getColumnIndex(DBStructurePsicopedagogia.EVENT_PSICO)); //LOS SUPRESSLINT SON INVENCIÓN MÍA, EN CASO DE ERROR AL USAR ESTE MÉTODO, BORRALO BEBEEE
            @SuppressLint("Range") String timePsico = cursor.getString(cursor.getColumnIndex(DBStructurePsicopedagogia.TIME_PSICO));
            @SuppressLint("Range") String DatePsico = cursor.getString(cursor.getColumnIndex(DBStructurePsicopedagogia.DATE_PSICO));
            @SuppressLint("Range") String monthPsico = cursor.getString(cursor.getColumnIndex(DBStructurePsicopedagogia.MONTH_PSICO));
            @SuppressLint("Range") String YearPsico = cursor.getString(cursor.getColumnIndex(DBStructurePsicopedagogia.YEAR_PSICO));
            EventsPsicopedagogia eventsPsicopedagogia = new EventsPsicopedagogia(eventPsico, timePsico, DatePsico, monthPsico, YearPsico);
            arrayList.add(eventsPsicopedagogia);

        }
        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }



    public CustomCalendarViewPsicopedagogia(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void SaveEvent (String event, String time, String date, String month, String year){


        DBOpenHelperPsicopedagogia dbOpenHelper = new DBOpenHelperPsicopedagogia(contextPsico); // SI NO, NO ANDA LA VARIABLE dbOpenHelper, revisar despues.
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, time, date, month, year, database);
        dbOpenHelper.close();
        Toast.makeText(contextPsico, "Event Saved", Toast.LENGTH_SHORT).show();


    }
    @SuppressLint("MissingInflatedId")
    private void InitializeLayout(){
        LayoutInflater inflater = (LayoutInflater)contextPsico.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout_psico, this);
        nextButtonPsico = view.findViewById(R.id.nextBtnPsico);
        previousButtonPsico = view.findViewById(R.id.previousBtnPsico);
        currentDatePsico = view.findViewById(R.id.current_DatePsico);
        gridViewPsico = view.findViewById(R.id.gridviewPsico);

    }
    private void SetUpCalendar(){
        String currwntDatePsico = dateFormatPsico.format(calendarPsico.getTime()); //EL NOMBRE DE LA VARIABLE ES RARO, SÍ.  PERO EL VIDEO QUE SEGUÍ LA PONE ASÍ.
        currentDatePsico.setText(currwntDatePsico);

        datesPsico.clear();
        Calendar monthCalendar = (Calendar) calendarPsico.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEventsPerMonth(monthFormatPsico.format(calendarPsico.getTime()), yearFormatPsico.format(calendarPsico.getTime()));

        while (datesPsico.size() < MAX_CALENDAR_DAYS) {
            datesPsico.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        myGridAdapterPsico = new MyGridAdapterPsicopedagogia(contextPsico, datesPsico, calendarPsico, eventsListPsico);
        gridViewPsico.setAdapter(myGridAdapterPsico);


    }

    private void CollectEventsPerMonth(String Month, String year){


        eventsListPsico.clear();
        DBOpenHelperPsicopedagogia dbOpenHelperPsicopedagogia = new DBOpenHelperPsicopedagogia(contextPsico); //REVISAR DESPUÉS PROBLEMA CON DB
        SQLiteDatabase database = dbOpenHelperPsicopedagogia.getReadableDatabase();
        Cursor cursor = dbOpenHelperPsicopedagogia.ReadEventsperMonth(Month, year, database);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String event = cursor.getString(cursor.getColumnIndex(DBStructurePsicopedagogia.EVENT_PSICO)); //LOS SUPRESSLINT SON INVENCIÓN MÍA, EN CASO DE ERROR AL USAR ESTE MÉTODO, BORRALO BEBEEE
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DBStructurePsicopedagogia.TIME_PSICO));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DBStructurePsicopedagogia.DATE_PSICO));
            @SuppressLint("Range") String month = cursor.getString(cursor.getColumnIndex(DBStructurePsicopedagogia.MONTH_PSICO));
            @SuppressLint("Range") String Year = cursor.getString(cursor.getColumnIndex(DBStructurePsicopedagogia.YEAR_PSICO));
            EventsPsicopedagogia eventsPsicopedagogia = new EventsPsicopedagogia(event, time, date, month, Year);
            eventsListPsico.add(eventsPsicopedagogia);

        }
        cursor.close();
        dbOpenHelperPsicopedagogia.close();

    }



}
