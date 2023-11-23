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

public class EventRecyclerAdapterEducacion extends RecyclerView.Adapter<EventRecyclerAdapterEducacion.MyViewHolderEducacion> {

    Context contextEdu;
    ArrayList<EventsEducacion> arrayListEdu;
    DBOpenHelperEducacion dbOpenHelperEdu;

    public EventRecyclerAdapterEducacion(Context contextEdu, ArrayList<EventsEducacion> arrayList) {
        this.contextEdu = contextEdu;
        this.arrayListEdu = arrayList;
    }


    @NonNull
    @Override
    public EventRecyclerAdapterEducacion.MyViewHolderEducacion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout_edu, parent, false);

        return new EventRecyclerAdapterEducacion.MyViewHolderEducacion(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerAdapterEducacion.MyViewHolderEducacion holder, @SuppressLint("RecyclerView") int position) {

        EventsEducacion eventsEdu = arrayListEdu.get(position);
        holder.EventEdu.setText(eventsEdu.getEVENTEDU());
        holder.dateTxtEdu.setText(eventsEdu.getDATEEDU());
        holder.TimeEdu.setText(eventsEdu.getTIMEEDU());
        holder.deleteEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCalendarEvent(eventsEdu.getEVENTEDU(), eventsEdu.getDATEEDU(), eventsEdu.getTIMEEDU()); //LLAMADA AL MÉTODO, FALTA REFRESH PARA QUE SE ACTUALIZE LOS EVENTOS.
                arrayListEdu.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayListEdu.size();
    }

    public class MyViewHolderEducacion extends RecyclerView.ViewHolder{

        TextView dateTxtEdu, EventEdu, TimeEdu;
        Button deleteEdu;
        public MyViewHolderEducacion(@NonNull View itemView) {
            super(itemView);
            dateTxtEdu = itemView.findViewById(R.id.eventdateEdu);
            EventEdu = itemView.findViewById(R.id.eventnameEdu);
            TimeEdu = itemView.findViewById(R.id.eventtimeEdu);
            deleteEdu = itemView.findViewById(R.id.deleteEdu);


        }
    }

    private void deleteCalendarEvent(String event, String date, String time){
        DBOpenHelperEducacion dbOpenHelperEdu = new DBOpenHelperEducacion(contextEdu);
        SQLiteDatabase database = dbOpenHelperEdu.getWritableDatabase();
        dbOpenHelperEdu.deleteEvent(event, date, time, database);
        dbOpenHelperEdu.close();
    }
    

}
