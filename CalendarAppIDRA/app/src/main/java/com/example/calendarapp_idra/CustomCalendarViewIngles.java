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

public class CustomCalendarViewIngles extends LinearLayout {

    ImageButton nextButtonIng, previousButtonIng;
    TextView currentDateIng;
    GridView gridViewIng;

    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendarIng = Calendar.getInstance(Locale.ENGLISH);
    Context contextIng;
    SimpleDateFormat dateFormatIng = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormatIng = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormatIng = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormateIng = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);



    MyGridAdapterIngles myGridAdapterIng;
    AlertDialog alertDialogIng;
    List<Date> datesIng = new ArrayList<>();
    List<EventsIngles> eventsListIng = new ArrayList<>();

    public CustomCalendarViewIngles(Context context) {
        super(context);
    }

    public CustomCalendarViewIngles(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.contextIng = context;
        InitializeLayout();
        SetUpCalendar();
        previousButtonIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarIng.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        nextButtonIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarIng.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridViewIng.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @SuppressLint("MissingInflatedId")
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout_ing, null);
                EditText EventName = addView.findViewById(R.id.eventnameIng);
                TextView EventTime = addView.findViewById(R.id.eventtimeIng);
                ImageButton SetTime = addView.findViewById(R.id.seteventtimeIng);
                Button AddEvent = addView.findViewById(R.id.addeventIng);
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
                final String date = eventDateFormateIng.format(datesIng.get(position));
                final String month = monthFormatIng.format(datesIng.get(position));
                final String year = yearFormatIng.format(datesIng.get(position));

                AddEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventName.getText().toString(),EventTime.getText().toString(), date, month, year);
                        SetUpCalendar();
                        alertDialogIng.dismiss();
                    }
                });

                builder.setView(addView);
                alertDialogIng = builder.create();
                alertDialogIng.show();
                alertDialogIng.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        SetUpCalendar();
                    }
                });

            }
        });

        gridViewIng.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("MissingInflatedId") //AGREGADO POR MI, EN CASO DE QUE NO ANDE, BORRAR Y BUSCAR OTRA SOLUCIÓN.
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String dateIng = eventDateFormateIng.format(datesIng.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout_ing, null);
                RecyclerView recyclerView = showView.findViewById(R.id.EventsRVIng);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapterIngles eventRecyclerAdapterIngles = new EventRecyclerAdapterIngles(showView.getContext(),
                        CollectEventByDate(dateIng));
                recyclerView.setAdapter(eventRecyclerAdapterIngles);
                eventRecyclerAdapterIngles.notifyDataSetChanged();

                builder.setView(showView);
                alertDialogIng = builder.create();
                alertDialogIng.show();


                return true;
            }
        });




    }

    private ArrayList<EventsIngles> CollectEventByDate(String date) {
        ArrayList<EventsIngles> arrayList = new ArrayList<>();
        DBOpenHelperIngles dbOpenHelper = new DBOpenHelperIngles(contextIng); //DBOPENHELPER TAMBIÉN PUESTO, NO SÉ SI ESTA BIEN.
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String eventIng = cursor.getString(cursor.getColumnIndex(DBStructureIngles.EVENT_ING)); //LOS SUPRESSLINT SON INVENCIÓN MÍA, EN CASO DE ERROR AL USAR ESTE MÉTODO, BORRALO BEBEEE
            @SuppressLint("Range") String timeIng = cursor.getString(cursor.getColumnIndex(DBStructureIngles.TIME_ING));
            @SuppressLint("Range") String DateIng = cursor.getString(cursor.getColumnIndex(DBStructureIngles.DATE_ING));
            @SuppressLint("Range") String monthIng = cursor.getString(cursor.getColumnIndex(DBStructureIngles.MONTH_ING));
            @SuppressLint("Range") String YearIng = cursor.getString(cursor.getColumnIndex(DBStructureIngles.YEAR_ING));
            EventsIngles eventsIngles = new EventsIngles(eventIng, timeIng, DateIng, monthIng, YearIng);
            arrayList.add(eventsIngles);

        }
        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }



    public CustomCalendarViewIngles(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void SaveEvent (String event, String time, String date, String month, String year){


        DBOpenHelperIngles dbOpenHelper = new DBOpenHelperIngles(contextIng); // SI NO, NO ANDA LA VARIABLE dbOpenHelper, revisar despues.
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, time, date, month, year, database);
        dbOpenHelper.close();
        Toast.makeText(contextIng, "Event Saved", Toast.LENGTH_SHORT).show();


    }
    @SuppressLint("MissingInflatedId")
    private void InitializeLayout(){
        LayoutInflater inflater = (LayoutInflater)contextIng.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout_ing, this);
        nextButtonIng = view.findViewById(R.id.nextBtnIng);
        previousButtonIng = view.findViewById(R.id.previousBtnIng);
        currentDateIng = view.findViewById(R.id.current_DateIng);
        gridViewIng = view.findViewById(R.id.gridviewIng);

    }
    private void SetUpCalendar(){
        String currwntDateIng = dateFormatIng.format(calendarIng.getTime()); //EL NOMBRE DE LA VARIABLE ES RARO, SÍ.  PERO EL VIDEO QUE SEGUÍ LA PONE ASÍ.
        currentDateIng.setText(currwntDateIng);

        datesIng.clear();
        Calendar monthCalendar = (Calendar) calendarIng.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEventsPerMonth(monthFormatIng.format(calendarIng.getTime()), yearFormatIng.format(calendarIng.getTime()));

        while (datesIng.size() < MAX_CALENDAR_DAYS) {
            datesIng.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        myGridAdapterIng = new MyGridAdapterIngles(contextIng, datesIng, calendarIng, eventsListIng);
        gridViewIng.setAdapter(myGridAdapterIng);


    }

    private void CollectEventsPerMonth(String Month, String year){


        eventsListIng.clear();
        DBOpenHelperIngles dbOpenHelperIngles = new DBOpenHelperIngles(contextIng); //REVISAR DESPUÉS PROBLEMA CON DB
        SQLiteDatabase database = dbOpenHelperIngles.getReadableDatabase();
        Cursor cursor = dbOpenHelperIngles.ReadEventsperMonth(Month, year, database);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String event = cursor.getString(cursor.getColumnIndex(DBStructureIngles.EVENT_ING)); //LOS SUPRESSLINT SON INVENCIÓN MÍA, EN CASO DE ERROR AL USAR ESTE MÉTODO, BORRALO BEBEEE
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DBStructureIngles.TIME_ING));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DBStructureIngles.DATE_ING));
            @SuppressLint("Range") String month = cursor.getString(cursor.getColumnIndex(DBStructureIngles.MONTH_ING));
            @SuppressLint("Range") String Year = cursor.getString(cursor.getColumnIndex(DBStructureIngles.YEAR_ING));
            EventsIngles eventsIngles = new EventsIngles(event, time, date, month, Year);
            eventsListIng.add(eventsIngles);

        }
        cursor.close();
        dbOpenHelperIngles.close();

    }
}
