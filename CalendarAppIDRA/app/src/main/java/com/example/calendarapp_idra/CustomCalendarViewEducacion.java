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

public class CustomCalendarViewEducacion extends LinearLayout {
    ImageButton nextButtonEdu, previousButtonEdu;
    TextView currentDateEdu;
    GridView gridViewEdu;

    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendarEdu = Calendar.getInstance(Locale.ENGLISH);
    Context contextEdu;
    SimpleDateFormat dateFormatEdu = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormatEdu = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormatEdu = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormateEdu = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);



    MyGridAdapterEducacion myGridAdapterEdu;
    AlertDialog alertDialogEdu;
    List<Date> datesEdu = new ArrayList<>();
    List<EventsEducacion> eventsListEdu = new ArrayList<>();

    public CustomCalendarViewEducacion(Context context) {
        super(context);
    }

    public CustomCalendarViewEducacion(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.contextEdu = context;
        InitializeLayout();
        SetUpCalendar();
        previousButtonEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarEdu.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        nextButtonEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarEdu.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridViewEdu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @SuppressLint("MissingInflatedId")
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout_edu, null);
                EditText EventName = addView.findViewById(R.id.eventnameEdu);
                TextView EventTime = addView.findViewById(R.id.eventtimeEdu);
                ImageButton SetTime = addView.findViewById(R.id.seteventtimeEdu);
                Button AddEvent = addView.findViewById(R.id.addeventEdu);
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
                final String date = eventDateFormateEdu.format(datesEdu.get(position));
                final String month = monthFormatEdu.format(datesEdu.get(position));
                final String year = yearFormatEdu.format(datesEdu.get(position));

                AddEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventName.getText().toString(),EventTime.getText().toString(), date, month, year);
                        SetUpCalendar();
                        alertDialogEdu.dismiss();
                    }
                });

                builder.setView(addView);
                alertDialogEdu = builder.create();
                alertDialogEdu.show();
                alertDialogEdu.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        SetUpCalendar();
                    }
                });

            }
        });

        gridViewEdu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("MissingInflatedId") //AGREGADO POR MI, EN CASO DE QUE NO ANDE, BORRAR Y BUSCAR OTRA SOLUCIÓN.
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String dateEdu = eventDateFormateEdu.format(datesEdu.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout_edu, null);
                RecyclerView recyclerView = showView.findViewById(R.id.EventsRVEdu);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapterEducacion eventRecyclerAdapterEducacion = new EventRecyclerAdapterEducacion(showView.getContext(),
                        CollectEventByDate(dateEdu));
                recyclerView.setAdapter(eventRecyclerAdapterEducacion);
                eventRecyclerAdapterEducacion.notifyDataSetChanged();

                builder.setView(showView);
                alertDialogEdu = builder.create();
                alertDialogEdu.show();


                return true;
            }
        });




    }

    private ArrayList<EventsEducacion> CollectEventByDate(String date) {
        ArrayList<EventsEducacion> arrayList = new ArrayList<>();
        DBOpenHelperEducacion dbOpenHelper = new DBOpenHelperEducacion(contextEdu); //DBOPENHELPER TAMBIÉN PUESTO, NO SÉ SI ESTA BIEN.
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String eventEdu = cursor.getString(cursor.getColumnIndex(DBStructureEducacion.EVENT_EDU)); //LOS SUPRESSLINT SON INVENCIÓN MÍA, EN CASO DE ERROR AL USAR ESTE MÉTODO, BORRALO BEBEEE
            @SuppressLint("Range") String timeEdu = cursor.getString(cursor.getColumnIndex(DBStructureEducacion.TIME_EDU));
            @SuppressLint("Range") String DateEdu = cursor.getString(cursor.getColumnIndex(DBStructureEducacion.DATE_EDU));
            @SuppressLint("Range") String monthEdu = cursor.getString(cursor.getColumnIndex(DBStructureEducacion.MONTH_EDU));
            @SuppressLint("Range") String YearEdu = cursor.getString(cursor.getColumnIndex(DBStructureEducacion.YEAR_EDU));
            EventsEducacion eventsEducacion = new EventsEducacion(eventEdu, timeEdu, DateEdu, monthEdu, YearEdu);
            arrayList.add(eventsEducacion);

        }
        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }



    public CustomCalendarViewEducacion(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void SaveEvent (String event, String time, String date, String month, String year){


        DBOpenHelperEducacion dbOpenHelper = new DBOpenHelperEducacion(contextEdu); // SI NO, NO ANDA LA VARIABLE dbOpenHelper, revisar despues.
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, time, date, month, year, database);
        dbOpenHelper.close();
        Toast.makeText(contextEdu, "Event Saved", Toast.LENGTH_SHORT).show();


    }
    @SuppressLint("MissingInflatedId")
    private void InitializeLayout(){
        LayoutInflater inflater = (LayoutInflater)contextEdu.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout_edu, this);
        nextButtonEdu = view.findViewById(R.id.nextBtnEdu);
        previousButtonEdu = view.findViewById(R.id.previousBtnEdu);
        currentDateEdu = view.findViewById(R.id.current_DateEdu);
        gridViewEdu = view.findViewById(R.id.gridviewEdu);

    }
    private void SetUpCalendar(){
        String currwntDateEdu = dateFormatEdu.format(calendarEdu.getTime()); //EL NOMBRE DE LA VARIABLE ES RARO, SÍ.  PERO EL VIDEO QUE SEGUÍ LA PONE ASÍ.
        currentDateEdu.setText(currwntDateEdu);

        datesEdu.clear();
        Calendar monthCalendar = (Calendar) calendarEdu.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEventsPerMonth(monthFormatEdu.format(calendarEdu.getTime()), yearFormatEdu.format(calendarEdu.getTime()));

        while (datesEdu.size() < MAX_CALENDAR_DAYS) {
            datesEdu.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        myGridAdapterEdu = new MyGridAdapterEducacion(contextEdu, datesEdu, calendarEdu, eventsListEdu);
        gridViewEdu.setAdapter(myGridAdapterEdu);


    }

    private void CollectEventsPerMonth(String Month, String year){


        eventsListEdu.clear();
        DBOpenHelperEducacion dbOpenHelperEducacion = new DBOpenHelperEducacion(contextEdu); //REVISAR DESPUÉS PROBLEMA CON DB
        SQLiteDatabase database = dbOpenHelperEducacion.getReadableDatabase();
        Cursor cursor = dbOpenHelperEducacion.ReadEventsperMonth(Month, year, database);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String event = cursor.getString(cursor.getColumnIndex(DBStructureEducacion.EVENT_EDU)); //LOS SUPRESSLINT SON INVENCIÓN MÍA, EN CASO DE ERROR AL USAR ESTE MÉTODO, BORRALO BEBEEE
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DBStructureEducacion.TIME_EDU));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DBStructureEducacion.DATE_EDU));
            @SuppressLint("Range") String month = cursor.getString(cursor.getColumnIndex(DBStructureEducacion.MONTH_EDU));
            @SuppressLint("Range") String Year = cursor.getString(cursor.getColumnIndex(DBStructureEducacion.YEAR_EDU));
            EventsEducacion eventsEducacion = new EventsEducacion(event, time, date, month, Year);
            eventsListEdu.add(eventsEducacion);

        }
        cursor.close();
        dbOpenHelperEducacion.close();

    }

}
