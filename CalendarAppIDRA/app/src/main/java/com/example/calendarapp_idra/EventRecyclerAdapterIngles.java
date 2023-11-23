package com.example.calendarapp_idra;

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

import java.util.ArrayList;

public class EventRecyclerAdapterIngles extends RecyclerView.Adapter<EventRecyclerAdapterIngles.MyViewHolderIngles> {

    Context contextIng;
    ArrayList<EventsIngles> arrayListIng;
    DBOpenHelperIngles dbOpenHelperIng;

    public EventRecyclerAdapterIngles(Context contextIng, ArrayList<EventsIngles> arrayList) {
        this.contextIng = contextIng;
        this.arrayListIng = arrayList;
    }


    @NonNull
    @Override
    public EventRecyclerAdapterIngles.MyViewHolderIngles onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout_ing, parent, false);

        return new EventRecyclerAdapterIngles.MyViewHolderIngles(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerAdapterIngles.MyViewHolderIngles holder, @SuppressLint("RecyclerView") int position) {

        EventsIngles eventsIng = arrayListIng.get(position);
        holder.EventIng.setText(eventsIng.getEVENTING());
        holder.DateTxtIng.setText(eventsIng.getDATEING());
        holder.TimeIng.setText(eventsIng.getTIMEING());
        holder.deleteIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCalendarEvent(eventsIng.getEVENTING(), eventsIng.getDATEING(), eventsIng.getTIMEING()); //LLAMADA AL MÉTODO, FALTA REFRESH PARA QUE SE ACTUALIZE LOS EVENTOS.
                arrayListIng.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayListIng.size();
    }

    public class MyViewHolderIngles extends RecyclerView.ViewHolder{

        TextView DateTxtIng, EventIng, TimeIng;
        Button deleteIng;
        public MyViewHolderIngles(@NonNull View itemView) {
            super(itemView);
            DateTxtIng = itemView.findViewById(R.id.eventdateIng);
            EventIng = itemView.findViewById(R.id.eventnameIng);
            TimeIng = itemView.findViewById(R.id.eventtimeIng);
            deleteIng = itemView.findViewById(R.id.deleteIng);


        }
    }

    private void deleteCalendarEvent(String event, String date, String time){
        DBOpenHelperIngles dbOpenHelperIng = new DBOpenHelperIngles(contextIng);
        SQLiteDatabase database = dbOpenHelperIng.getWritableDatabase();
        dbOpenHelperIng.deleteEvent(event, date, time, database);
        dbOpenHelperIng.close();
    }
}
