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

public class MyGridAdapterSeguridad extends ArrayAdapter {
    List<Date> datesSeg;
    Calendar currentDateSeg;
    List<EventsSeguridad> eventsSeg;
    LayoutInflater inflaterSeg;


    public MyGridAdapterSeguridad(@NonNull Context context, List<Date> dates, Calendar currentDate, List<EventsSeguridad> events) {
        super(context, R.layout.single_cell_layout_seg);

        this.datesSeg = dates;
        this.currentDateSeg = currentDate;
        this.eventsSeg = events;
        inflaterSeg = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDateSeg = datesSeg.get(position);
        Calendar dateCalendarSeg = Calendar.getInstance();
        dateCalendarSeg.setTime(monthDateSeg);

        int DayNoSeg = dateCalendarSeg.get(Calendar.DAY_OF_MONTH);
        int displayMonthSeg = dateCalendarSeg.get(Calendar.MONTH) + 1;
        int displayYearSeg = dateCalendarSeg.get(Calendar.YEAR);
        int currentMonthSeg = currentDateSeg.get(Calendar.MONTH)+1;
        int currentYearSeg = currentDateSeg.get(Calendar.YEAR);



        View view = convertView;
        if (view == null) {
            view = inflaterSeg.inflate(R.layout.single_cell_layout_seg, parent, false);


        }

        if (displayMonthSeg == currentMonthSeg && displayYearSeg == currentYearSeg){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.lightyellow)); //PARA CAMBIAR EL COLOR RESALTADOS DE LOS DÍAS DEL MES. EL COLOR ESTA ALOJADO EN RES/VALUES/COLORS.XML
        }
        else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        TextView Day_NumberSeg = view.findViewById(R.id.calendar_day_seg);
        TextView EventNumberSeg = view.findViewById(R.id.events_id_seg);
        Day_NumberSeg.setText(String.valueOf(DayNoSeg));
        Calendar eventCalendarSeg = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < eventsSeg.size(); i++){
            eventCalendarSeg.setTime(ConvertStringToDate(eventsSeg.get(i).getDATESEG()));

            if(DayNoSeg == eventCalendarSeg.get(Calendar.DAY_OF_MONTH) && displayMonthSeg == eventCalendarSeg.get(Calendar.MONTH)+1
                    && displayYearSeg == eventCalendarSeg.get(Calendar.YEAR)){


                arrayList.add(eventsSeg.get(i).getEVENTSEG());
                EventNumberSeg.setText(arrayList.size()+" Events");
            }
        }

        return view;
    }

    private Date ConvertStringToDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date dateSeg = null;
        try {
            dateSeg = format.parse(eventDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return dateSeg;
    }

    @Override
    public int getCount() {
        return datesSeg.size();
    }


    @Override
    public int getPosition(@Nullable Object item) {
        return datesSeg.indexOf(item);
    }


    @Nullable
    @Override
    public Object getItem(int position) {
        return datesSeg.get(position);
    }
}
