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

public class MyGridAdapterIngles extends ArrayAdapter {
    List<Date> datesIng;
    Calendar currentDateIng;
    List<EventsIngles> eventsIng;
    LayoutInflater inflaterIng;


    public MyGridAdapterIngles(@NonNull Context context, List<Date> dates, Calendar currentDate, List<EventsIngles> events) {
        super(context, R.layout.single_cell_layout_ing);

        this.datesIng = dates;
        this.currentDateIng = currentDate;
        this.eventsIng = events;
        inflaterIng = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDateIng = datesIng.get(position);
        Calendar dateCalendarIng = Calendar.getInstance();
        dateCalendarIng.setTime(monthDateIng);

        int DayNoIng = dateCalendarIng.get(Calendar.DAY_OF_MONTH);
        int displayMonthIng = dateCalendarIng.get(Calendar.MONTH) + 1;
        int displayYearIng = dateCalendarIng.get(Calendar.YEAR);
        int currentMonthIng = currentDateIng.get(Calendar.MONTH)+1;
        int currentYearIng = currentDateIng.get(Calendar.YEAR);



        View view = convertView;
        if (view == null) {
            view = inflaterIng.inflate(R.layout.single_cell_layout_ing, parent, false);


        }

        if (displayMonthIng == currentMonthIng && displayYearIng == currentYearIng){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.lightyellow)); //PARA CAMBIAR EL COLOR RESALTADOS DE LOS DÍAS DEL MES. EL COLOR ESTA ALOJADO EN RES/VALUES/COLORS.XML
        }
        else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        TextView Day_NumberIng = view.findViewById(R.id.calendar_day_ing);
        TextView EventNumberIng = view.findViewById(R.id.events_id_ing);
        Day_NumberIng.setText(String.valueOf(DayNoIng));
        Calendar eventCalendarIng = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < eventsIng.size(); i++){
            eventCalendarIng.setTime(ConvertStringToDate(eventsIng.get(i).getDATEING()));

            if(DayNoIng == eventCalendarIng.get(Calendar.DAY_OF_MONTH) && displayMonthIng == eventCalendarIng.get(Calendar.MONTH)+1
                    && displayYearIng == eventCalendarIng.get(Calendar.YEAR)){


                arrayList.add(eventsIng.get(i).getEVENTING());
                EventNumberIng.setText(arrayList.size()+" Events");
            }
        }

        return view;
    }

    private Date ConvertStringToDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date dateIng = null;
        try {
            dateIng = format.parse(eventDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return dateIng;
    }

    @Override
    public int getCount() {
        return datesIng.size();
    }


    @Override
    public int getPosition(@Nullable Object item) {
        return datesIng.indexOf(item);
    }


    @Nullable
    @Override
    public Object getItem(int position) {
        return datesIng.get(position);
    }
}
