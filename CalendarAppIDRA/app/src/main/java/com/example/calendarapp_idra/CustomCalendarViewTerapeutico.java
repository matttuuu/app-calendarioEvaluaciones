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

public class CustomCalendarViewTerapeutico extends LinearLayout {
    ImageButton nextButtonTera, previousButtonTera;
    TextView currentDateTera;
    GridView gridViewTera;

    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendarTera = Calendar.getInstance(Locale.ENGLISH);
    Context contextTera;
    SimpleDateFormat dateFormatTera = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormatTera = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormatTera = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormateTera = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);



    MyGridAdapterTerapeutico myGridAdapterTera;
    AlertDialog alertDialogTera;
    List<Date> datesTera = new ArrayList<>();
    List<EventsTerapeutico> eventsListTera = new ArrayList<>();

    public CustomCalendarViewTerapeutico(Context context) {
        super(context);
    }

    public CustomCalendarViewTerapeutico(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.contextTera = context;
        InitializeLayout();
        SetUpCalendar();
        previousButtonTera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarTera.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        nextButtonTera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarTera.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridViewTera.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @SuppressLint("MissingInflatedId")
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout_tera, null);
                EditText EventName = addView.findViewById(R.id.eventnameTera);
                TextView EventTime = addView.findViewById(R.id.eventtimeTera);
                ImageButton SetTime = addView.findViewById(R.id.seteventtimeTera);
                Button AddEvent = addView.findViewById(R.id.addeventTera);
                SetTime.setOnClickListener(new OnClickListener() {
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
                final String date = eventDateFormateTera.format(datesTera.get(position));
                final String month = monthFormatTera.format(datesTera.get(position));
                final String year = yearFormatTera.format(datesTera.get(position));

                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventName.getText().toString(),EventTime.getText().toString(), date, month, year);
                        SetUpCalendar();
                        alertDialogTera.dismiss();
                    }
                });

                builder.setView(addView);
                alertDialogTera = builder.create();
                alertDialogTera.show();
                alertDialogTera.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        SetUpCalendar();
                    }
                });

            }
        });

        gridViewTera.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("MissingInflatedId") //AGREGADO POR MI, EN CASO DE QUE NO ANDE, BORRAR Y BUSCAR OTRA SOLUCIÓN.
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String dateTera = eventDateFormateTera.format(datesTera.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout_tera, null);
                RecyclerView recyclerView = showView.findViewById(R.id.EventsRVTera);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapterTerapeutico eventRecyclerAdapterTerapeutico = new EventRecyclerAdapterTerapeutico(showView.getContext(),
                        CollectEventByDate(dateTera));
                recyclerView.setAdapter(eventRecyclerAdapterTerapeutico);
                eventRecyclerAdapterTerapeutico.notifyDataSetChanged();

                builder.setView(showView);
                alertDialogTera = builder.create();
                alertDialogTera.show();


                return true;
            }
        });




    }

    private ArrayList<EventsTerapeutico> CollectEventByDate(String date) {
        ArrayList<EventsTerapeutico> arrayList = new ArrayList<>();
        DBOpenHelperTerapeutico dbOpenHelper = new DBOpenHelperTerapeutico(contextTera); //DBOPENHELPER TAMBIÉN PUESTO, NO SÉ SI ESTA BIEN.
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String eventTera = cursor.getString(cursor.getColumnIndex(DBStructureTerapeutico.EVENT_TERA)); //LOS SUPRESSLINT SON INVENCIÓN MÍA, EN CASO DE ERROR AL USAR ESTE MÉTODO, BORRALO BEBEEE
            @SuppressLint("Range") String timeTera = cursor.getString(cursor.getColumnIndex(DBStructureTerapeutico.TIME_TERA));
            @SuppressLint("Range") String DateTera = cursor.getString(cursor.getColumnIndex(DBStructureTerapeutico.DATE_TERA));
            @SuppressLint("Range") String monthTera = cursor.getString(cursor.getColumnIndex(DBStructureTerapeutico.MONTH_TERA));
            @SuppressLint("Range") String YearTera = cursor.getString(cursor.getColumnIndex(DBStructureTerapeutico.YEAR_TERA));
            EventsTerapeutico eventsTerapeutico = new EventsTerapeutico(eventTera, timeTera, DateTera, monthTera, YearTera);
            arrayList.add(eventsTerapeutico);

        }
        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }



    public CustomCalendarViewTerapeutico(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void SaveEvent (String event, String time, String date, String month, String year){


        DBOpenHelperTerapeutico dbOpenHelper = new DBOpenHelperTerapeutico(contextTera); // SI NO, NO ANDA LA VARIABLE dbOpenHelper, revisar despues.
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, time, date, month, year, database);
        dbOpenHelper.close();
        Toast.makeText(contextTera, "Event Saved", Toast.LENGTH_SHORT).show();


    }
    @SuppressLint("MissingInflatedId")
    private void InitializeLayout(){
        LayoutInflater inflater = (LayoutInflater)contextTera.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout_tera, this);
        nextButtonTera = view.findViewById(R.id.nextBtnTera);
        previousButtonTera = view.findViewById(R.id.previousBtnTera);
        currentDateTera = view.findViewById(R.id.current_DateTera);
        gridViewTera = view.findViewById(R.id.gridviewTera);

    }
    private void SetUpCalendar(){
        String currwntDateTera = dateFormatTera.format(calendarTera.getTime()); //EL NOMBRE DE LA VARIABLE ES RARO, SÍ.  PERO EL VIDEO QUE SEGUÍ LA PONE ASÍ.
        currentDateTera.setText(currwntDateTera);

        datesTera.clear();
        Calendar monthCalendar = (Calendar) calendarTera.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEventsPerMonth(monthFormatTera.format(calendarTera.getTime()), yearFormatTera.format(calendarTera.getTime()));

        while (datesTera.size() < MAX_CALENDAR_DAYS) {
            datesTera.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        myGridAdapterTera = new MyGridAdapterTerapeutico(contextTera, datesTera, calendarTera, eventsListTera);
        gridViewTera.setAdapter(myGridAdapterTera);


    }

    private void CollectEventsPerMonth(String Month, String year){


        eventsListTera.clear();
        DBOpenHelperTerapeutico dbOpenHelperTerapeutico = new DBOpenHelperTerapeutico(contextTera); //REVISAR DESPUÉS PROBLEMA CON DB
        SQLiteDatabase database = dbOpenHelperTerapeutico.getReadableDatabase();
        Cursor cursor = dbOpenHelperTerapeutico.ReadEventsperMonth(Month, year, database);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String event = cursor.getString(cursor.getColumnIndex(DBStructureTerapeutico.EVENT_TERA)); //LOS SUPRESSLINT SON INVENCIÓN MÍA, EN CASO DE ERROR AL USAR ESTE MÉTODO, BORRALO BEBEEE
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DBStructureTerapeutico.TIME_TERA));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DBStructureTerapeutico.DATE_TERA));
            @SuppressLint("Range") String month = cursor.getString(cursor.getColumnIndex(DBStructureTerapeutico.MONTH_TERA));
            @SuppressLint("Range") String Year = cursor.getString(cursor.getColumnIndex(DBStructureTerapeutico.YEAR_TERA));
            EventsTerapeutico eventsTerapeutico = new EventsTerapeutico(event, time, date, month, Year);
            eventsListTera.add(eventsTerapeutico);

        }
        cursor.close();
        dbOpenHelperTerapeutico.close();

    }


}
