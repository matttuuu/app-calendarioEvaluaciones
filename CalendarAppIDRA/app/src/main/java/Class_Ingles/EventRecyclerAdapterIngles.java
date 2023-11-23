package Class_Ingles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarapp_idra.DBOpenHelper;

import com.example.calendarapp_idra.R;

import java.util.ArrayList;

public class EventRecyclerAdapterIngles extends RecyclerView.Adapter<EventRecyclerAdapterIngles.MyViewHolderIngles> {
    Context context;
    ArrayList<EventsIngles> arrayListIngles;
    DBOpenHelper dbhelper;

    public EventRecyclerAdapterIngles(Context context, ArrayList<EventsIngles> arrayList){
        this.context = context;
        this.arrayListIngles = arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayListIngles.size();
    }




    @NonNull
    @Override
    public EventRecyclerAdapterIngles.MyViewHolderIngles onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout_ingles, parent, false);

        return new EventRecyclerAdapterIngles.MyViewHolderIngles(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EventRecyclerAdapterIngles.MyViewHolderIngles holder, @SuppressLint("RecyclerView") int position) {

        EventsIngles events = arrayListIngles.get(position);
        holder.EventIngles.setText(events.getEVENTINGLES());
        holder.DateTxtIngles.setText(events.getDATEINGLES());
        holder.TimeIngles.setText(events.getTIMEINGLES());
        holder.deleteIngles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCalendarEvent(events.getEVENTINGLES(), events.getDATEINGLES(), events.getTIMEINGLES()); //LLAMADA AL MÉTODO, FALTA REFRESH PARA QUE SE ACTUALIZE LOS EVENTOS.
                arrayListIngles.remove(position);
                notifyDataSetChanged();
            }
        });

    }



    public class MyViewHolderIngles extends RecyclerView.ViewHolder{

        TextView DateTxtIngles, EventIngles, TimeIngles;
        Button deleteIngles;
        public MyViewHolderIngles(@NonNull View itemView) {
            super(itemView);
            DateTxtIngles = itemView.findViewById(R.id.eventdateIngles);
            EventIngles = itemView.findViewById(R.id.eventnameIngles);
            TimeIngles = itemView.findViewById(R.id.eventtimeIngles);
            deleteIngles = itemView.findViewById(R.id.deleteIngles);


        }
    }
    private void deleteCalendarEvent(String event, String date, String time){
        DBOpenHelperIngles dbhelper = new DBOpenHelperIngles(context);
        SQLiteDatabase database = dbhelper.getWritableDatabase();
        dbhelper.deleteEvent(event, date, time, database);
        dbhelper.close();
    }
}
