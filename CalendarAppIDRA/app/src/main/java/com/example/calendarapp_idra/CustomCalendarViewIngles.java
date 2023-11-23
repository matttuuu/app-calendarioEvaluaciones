package com.example.calendarapp_idra;

import android.widget.LinearLayout;

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

import Class_Ingles.DBOpenHelperIngles;
import Class_Ingles.DBStructureIngles;
import Class_Ingles.EventRecyclerAdapterIngles;
import Class_Ingles.EventsIngles;
import Class_Ingles.MyGridAdapterIngles;

public class CustomCalendarViewIngles extends LinearLayout {

    ImageButton nextButton, previousButton;
    TextView currentDate;
    GridView gridView;

    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat= new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormat= new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);



    MyGridAdapterIngles myGridAdapter;
    AlertDialog alertDialogIngles;
    List<Date> dates= new ArrayList<>();
    List<EventsIngles> eventsList = new ArrayList<>();

    public CustomCalendarViewIngles(Context context) {
        super(context);
    }

    public CustomCalendarViewIngles(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        this.context = context; //Probar si es mas eficiente dejar variables con nombres mas genericos en vez de especificar "_Ingles" O "_INGLES"
        InitializeLayout();
        SetUpCalendar();

        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @SuppressLint("MissingInflatedId")
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout_ingles, null);
                EditText EventName = addView.findViewById(R.id.eventnameIngles); //CAMBIAR IDS DE LAYOUT XML
                TextView EventTime = addView.findViewById(R.id.eventtimeIngles); //
                ImageButton SetTime = addView.findViewById(R.id.seteventtimeIngles); //
                Button AddEvent = addView.findViewById(R.id.addeventIngles); // *
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
                final String date = eventDateFormat.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));

                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventName.getText().toString(),EventTime.getText().toString(), date, month, year);
                        SetUpCalendar();
                        alertDialogIngles.dismiss();
                    }
                });

                builder.setView(addView);
                alertDialogIngles = builder.create();
                alertDialogIngles.show();
                alertDialogIngles.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        SetUpCalendar();
                    }
                });

            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("MissingInflatedId") //AGREGADO POR MI, EN CASO DE QUE NO ANDE, BORRAR Y BUSCAR OTRA SOLUCIÓN.
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String date = eventDateFormat.format(dates.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout_ingles, null);
                RecyclerView recyclerView = showView.findViewById(R.id.EventsRVIngles);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapterIngles eventRecyclerAdapterIngles = new EventRecyclerAdapterIngles(showView.getContext(),
                        CollectEventByDate(date));
                recyclerView.setAdapter(eventRecyclerAdapterIngles);
                eventRecyclerAdapterIngles.notifyDataSetChanged();

                builder.setView(showView);
                alertDialogIngles = builder.create();
                alertDialogIngles.show();


                return true;
            }
        });

    }

    private ArrayList<EventsIngles> CollectEventByDate(String date) {
        ArrayList<EventsIngles> arrayList = new ArrayList<>();
        DBOpenHelperIngles dbOpenHelper = new DBOpenHelperIngles(context); //DBOPENHELPER TAMBIÉN PUESTO, NO SÉ SI ESTA BIEN.
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String event = cursor.getString(cursor.getColumnIndex(DBStructureIngles.EVENT_INGLES)); //LOS SUPRESSLINT SON INVENCIÓN MÍA, EN CASO DE ERROR AL USAR ESTE MÉTODO, BORRALO BEBEEE
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DBStructureIngles.TIME_INGLES));
            @SuppressLint("Range") String Date = cursor.getString(cursor.getColumnIndex(DBStructureIngles.DATE_INGLES));
            @SuppressLint("Range") String month = cursor.getString(cursor.getColumnIndex(DBStructureIngles.MONTH_INGLES));
            @SuppressLint("Range") String Year = cursor.getString(cursor.getColumnIndex(DBStructureIngles.YEAR_INGLES));
            EventsIngles events = new EventsIngles(event, time, Date, month, Year);
            arrayList.add(events);

        }
        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }

    public CustomCalendarViewIngles(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void SaveEvent (String event, String time, String date, String month, String year){


        DBOpenHelperIngles dbOpenHelper = new DBOpenHelperIngles(context); // SI NO, NO ANDA LA VARIABLE dbOpenHelper, revisar despues.
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, time, date, month, year, database);
        dbOpenHelper.close();
        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show();


    }

    @SuppressLint("MissingInflatedId")
    private void InitializeLayout(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_ingles_layout, this); //
        nextButton = view.findViewById(R.id.nextBtnIngles); //va a tirar error cuando ponga distintos ids en el layout
        previousButton = view.findViewById(R.id.previousBtnIngles);
        currentDate = view.findViewById(R.id.current_DateIngles);
        gridView = view.findViewById(R.id.gridviewIngles);

    }

    private void SetUpCalendar(){
        String currwntDateIngles = dateFormat.format(calendar.getTime()); //EL NOMBRE DE LA VARIABLE ES RARO, SÍ.  PERO EL VIDEO QUE SEGUÍ LA PONE ASÍ.
        currentDate.setText(currwntDateIngles);

        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));

        while (dates.size() < MAX_CALENDAR_DAYS) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        myGridAdapter = new MyGridAdapterIngles(context, dates, calendar, eventsList);
        gridView.setAdapter(myGridAdapter);


    }

    private void CollectEventsPerMonth(String Month, String year){


        eventsList.clear();
        DBOpenHelperIngles dbhelper = new DBOpenHelperIngles(context); //REVISAR DESPUÉS PROBLEMA CON DB
        SQLiteDatabase database = dbhelper.getReadableDatabase();
        Cursor cursor = dbhelper.ReadEventsperMonth(Month, year, database);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String event = cursor.getString(cursor.getColumnIndex(DBStructureIngles.EVENT_INGLES)); //LOS SUPRESSLINT SON INVENCIÓN MÍA, EN CASO DE ERROR AL USAR ESTE MÉTODO, BORRALO BEBEEE
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DBStructureIngles.TIME_INGLES));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DBStructureIngles.DATE_INGLES));
            @SuppressLint("Range") String month = cursor.getString(cursor.getColumnIndex(DBStructureIngles.MONTH_INGLES));
            @SuppressLint("Range") String Year = cursor.getString(cursor.getColumnIndex(DBStructureIngles.YEAR_INGLES));
            EventsIngles events = new EventsIngles(event, time, date, month, Year);
            eventsList.add(events);

        }
        cursor.close();
        dbhelper.close();

    }

}
