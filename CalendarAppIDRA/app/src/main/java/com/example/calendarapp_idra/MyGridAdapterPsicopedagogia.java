package com.example.calendarapp_idra;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class MyGridAdapterPsicopedagogia extends ArrayAdapter {
    List<Date> datesPsico;
    Calendar currentDatePsico;
    List<EventsPsicopedagogia> eventsPsico;
    LayoutInflater inflaterPsico;


    public MyGridAdapterPsicopedagogia(@NonNull Context context, List<Date> dates, Calendar currentDate, List<EventsPsicopedagogia> events) {
        super(context, R.layout.single_cell_layout_psico);

        this.datesPsico = dates;
        this.currentDatePsico = currentDate;
        this.eventsPsico = events;
        inflaterPsico = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDatePsico = datesPsico.get(position);
        Calendar dateCalendarPsico = Calendar.getInstance();
        dateCalendarPsico.setTime(monthDatePsico);

        int DayNoPsico = dateCalendarPsico.get(Calendar.DAY_OF_MONTH);
        int displayMonthPsico = dateCalendarPsico.get(Calendar.MONTH) + 1;
        int displayYearPsico = dateCalendarPsico.get(Calendar.YEAR);
        int currentMonthPsico = currentDatePsico.get(Calendar.MONTH)+1;
        int currentYearPsico = currentDatePsico.get(Calendar.YEAR);



        View view = convertView;
        if (view == null) {
            view = inflaterPsico.inflate(R.layout.single_cell_layout_psico, parent, false);


        }

        if (displayMonthPsico == currentMonthPsico && displayYearPsico == currentYearPsico){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.lilac)); //PARA CAMBIAR EL COLOR RESALTADOS DE LOS DÍAS DEL MES. EL COLOR ESTA ALOJADO EN RES/VALUES/COLORS.XML
        }
        else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        TextView Day_NumberPsico = view.findViewById(R.id.calendar_day_psico);
        TextView EventNumberPsico = view.findViewById(R.id.events_id_psico);
        Day_NumberPsico.setText(String.valueOf(DayNoPsico));
        Calendar eventCalendarPsico = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < eventsPsico.size(); i++){
            eventCalendarPsico.setTime(ConvertStringToDate(eventsPsico.get(i).getDATEPSICO()));

            if(DayNoPsico == eventCalendarPsico.get(Calendar.DAY_OF_MONTH) && displayMonthPsico == eventCalendarPsico.get(Calendar.MONTH)+1
                    && displayYearPsico == eventCalendarPsico.get(Calendar.YEAR)){


                arrayList.add(eventsPsico.get(i).getEVENTPSICO());
                EventNumberPsico.setText(arrayList.size()+" Events");
            }
        }

        return view;
    }

    private Date ConvertStringToDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date datePsico = null;
        try {
            datePsico = format.parse(eventDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return datePsico;
    }

    @Override
    public int getCount() {
        return datesPsico.size();
    }


    @Override
    public int getPosition(@Nullable Object item) {
        return datesPsico.indexOf(item);
    }


    @Nullable
    @Override
    public Object getItem(int position) {
        return datesPsico.get(position);
    }
}
