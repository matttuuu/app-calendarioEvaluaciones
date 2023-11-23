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

public class MyGridAdapterEducacion extends ArrayAdapter {
    List<Date> datesEdu;
    Calendar currentDateEdu;
    List<EventsEducacion> eventsEdu;
    LayoutInflater inflaterEdu;


    public MyGridAdapterEducacion(@NonNull Context context, List<Date> dates, Calendar currentDate, List<EventsEducacion> events) {
        super(context, R.layout.single_cell_layout_edu);

        this.datesEdu = dates;
        this.currentDateEdu = currentDate;
        this.eventsEdu = events;
        inflaterEdu = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDateEdu = datesEdu.get(position);
        Calendar dateCalendarEdu = Calendar.getInstance();
        dateCalendarEdu.setTime(monthDateEdu);

        int DayNoEdu = dateCalendarEdu.get(Calendar.DAY_OF_MONTH);
        int displayMonthEdu = dateCalendarEdu.get(Calendar.MONTH) + 1;
        int displayYearEdu = dateCalendarEdu.get(Calendar.YEAR);
        int currentMonthEdu = currentDateEdu.get(Calendar.MONTH)+1;
        int currentYearEdu = currentDateEdu.get(Calendar.YEAR);



        View view = convertView;
        if (view == null) {
            view = inflaterEdu.inflate(R.layout.single_cell_layout_edu, parent, false);


        }

        if (displayMonthEdu == currentMonthEdu && displayYearEdu == currentYearEdu){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.lightyellow)); //PARA CAMBIAR EL COLOR RESALTADOS DE LOS DÍAS DEL MES. EL COLOR ESTA ALOJADO EN RES/VALUES/COLORS.XML
        }
        else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        TextView Day_NumberEdu = view.findViewById(R.id.calendar_day_edu);
        TextView EventNumberEdu = view.findViewById(R.id.events_id_edu);
        Day_NumberEdu.setText(String.valueOf(DayNoEdu));
        Calendar eventCalendarEdu = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < eventsEdu.size(); i++){
            eventCalendarEdu.setTime(ConvertStringToDate(eventsEdu.get(i).getDATEEDU()));

            if(DayNoEdu == eventCalendarEdu.get(Calendar.DAY_OF_MONTH) && displayMonthEdu == eventCalendarEdu.get(Calendar.MONTH)+1
                    && displayYearEdu == eventCalendarEdu.get(Calendar.YEAR)){


                arrayList.add(eventsEdu.get(i).getEVENTEDU());
                EventNumberEdu.setText(arrayList.size()+" Events");
            }
        }

        return view;
    }

    private Date ConvertStringToDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date dateEdu = null;
        try {
            dateEdu = format.parse(eventDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return dateEdu;
    }

    @Override
    public int getCount() {
        return datesEdu.size();
    }


    @Override
    public int getPosition(@Nullable Object item) {
        return datesEdu.indexOf(item);
    }


    @Nullable
    @Override
    public Object getItem(int position) {
        return datesEdu.get(position);
    }
}
