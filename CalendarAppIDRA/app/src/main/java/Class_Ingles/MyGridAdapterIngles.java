package Class_Ingles;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.calendarapp_idra.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyGridAdapterIngles extends ArrayAdapter {

    List<Date> datesIngles;
    Calendar currentDateIngles;
    List<EventsIngles> eventsIngles;
    LayoutInflater inflaterIngles;

    public MyGridAdapterIngles(@NonNull Context context, List<Date> dates,Calendar currentDate, List<EventsIngles> events) {
        super(context, R.layout.single_cell_layout_tera);

        this.datesIngles = dates;
        this.currentDateIngles = currentDate;
        this.eventsIngles = events;
        inflaterIngles = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDateIngles = datesIngles.get(position);
        Calendar dateCalendarIngles = Calendar.getInstance();
        dateCalendarIngles.setTime(monthDateIngles);

        int DayNoIngles = dateCalendarIngles.get(Calendar.DAY_OF_MONTH);
        int displayMonthIngles = dateCalendarIngles.get(Calendar.MONTH) + 1;
        int displayYearIngles = dateCalendarIngles.get(Calendar.YEAR);
        int currentMonthIngles = currentDateIngles.get(Calendar.MONTH)+1;
        int currentYearIngles = currentDateIngles.get(Calendar.YEAR);



        View view = convertView;
        if (view == null) {
            view = inflaterIngles.inflate(R.layout.single_cell_layout_tera, parent, false);


        }

        if (displayMonthIngles == currentMonthIngles && displayYearIngles == currentYearIngles){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.lightyellow)); //PARA CAMBIAR EL COLOR RESALTADOS DE LOS DÍAS DEL MES. EL COLOR ESTA ALOJADO EN RES/VALUES/COLORS.XML
        }
        else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        TextView Day_NumberIngles = view.findViewById(R.id.calendar_day_tera);
        TextView EventNumberIngles = view.findViewById(R.id.events_id_tera);
        Day_NumberIngles.setText(String.valueOf(DayNoIngles));
        Calendar eventCalendarIngles = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < eventsIngles.size(); i++){
            eventCalendarIngles.setTime(ConvertStringToDate(eventsIngles.get(i).getDATEINGLES()));

            if(DayNoIngles == eventCalendarIngles.get(Calendar.DAY_OF_MONTH) && displayMonthIngles == eventCalendarIngles.get(Calendar.MONTH)+1
                    && displayYearIngles == eventCalendarIngles.get(Calendar.YEAR)){


                arrayList.add(eventsIngles.get(i).getEVENTINGLES());
                EventNumberIngles.setText(arrayList.size()+" Events");
            }
        }

        return view;
    }

    private Date ConvertStringToDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date dateIngles = null;
        try {
            dateIngles = format.parse(eventDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return dateIngles;
    }

    @Override
    public int getCount() {
        return datesIngles.size();
    }


    @Override
    public int getPosition(@Nullable Object item) {
        return datesIngles.indexOf(item);
    }


    @Nullable
    @Override
    public Object getItem(int position) {
        return datesIngles.get(position);
    }

}
