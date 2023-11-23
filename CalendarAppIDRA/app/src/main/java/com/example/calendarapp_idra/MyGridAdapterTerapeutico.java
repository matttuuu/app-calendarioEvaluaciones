package com.example.calendarapp_idra;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyGridAdapterTerapeutico extends ArrayAdapter {
    List<Date> datesTera;
    Calendar currentDateTera;
    List<EventsTerapeutico> eventsTera;
    LayoutInflater inflaterTera;


    public MyGridAdapterTerapeutico(@NonNull Context context, List<Date> dates,Calendar currentDate, List<EventsTerapeutico> events) {
        super(context, R.layout.single_cell_layout_tera);

        this.datesTera = dates;
        this.currentDateTera = currentDate;
        this.eventsTera = events;
        inflaterTera = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDateTera = datesTera.get(position);
        Calendar dateCalendarTera = Calendar.getInstance();
        dateCalendarTera.setTime(monthDateTera);

        int DayNoTera = dateCalendarTera.get(Calendar.DAY_OF_MONTH);
        int displayMonthTera = dateCalendarTera.get(Calendar.MONTH) + 1;
        int displayYearTera = dateCalendarTera.get(Calendar.YEAR);
        int currentMonthTera = currentDateTera.get(Calendar.MONTH)+1;
        int currentYearTera = currentDateTera.get(Calendar.YEAR);



        View view = convertView;
        if (view == null) {
            view = inflaterTera.inflate(R.layout.single_cell_layout_tera, parent, false);


        }

        if (displayMonthTera == currentMonthTera && displayYearTera == currentYearTera){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.lightyellow)); //PARA CAMBIAR EL COLOR RESALTADOS DE LOS DÍAS DEL MES. EL COLOR ESTA ALOJADO EN RES/VALUES/COLORS.XML
        }
        else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        TextView Day_NumberTera = view.findViewById(R.id.calendar_day_tera);
        TextView EventNumberTera = view.findViewById(R.id.events_id_tera);
        Day_NumberTera.setText(String.valueOf(DayNoTera));
        Calendar eventCalendarTera = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < eventsTera.size(); i++){
            eventCalendarTera.setTime(ConvertStringToDate(eventsTera.get(i).getDATETERA()));

            if(DayNoTera == eventCalendarTera.get(Calendar.DAY_OF_MONTH) && displayMonthTera == eventCalendarTera.get(Calendar.MONTH)+1
                    && displayYearTera == eventCalendarTera.get(Calendar.YEAR)){


                arrayList.add(eventsTera.get(i).getEVENTTERA());
                EventNumberTera.setText(arrayList.size()+" Events");
            }
        }

        return view;
    }

    private Date ConvertStringToDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date dateTera = null;
        try {
            dateTera = format.parse(eventDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return dateTera;
    }

    @Override
    public int getCount() {
        return datesTera.size();
    }


    @Override
    public int getPosition(@Nullable Object item) {
        return datesTera.indexOf(item);
    }


    @Nullable
    @Override
    public Object getItem(int position) {
        return datesTera.get(position);
    }
}
