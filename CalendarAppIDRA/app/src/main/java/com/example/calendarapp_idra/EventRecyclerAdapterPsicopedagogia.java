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

public class EventRecyclerAdapterPsicopedagogia extends RecyclerView.Adapter<EventRecyclerAdapterPsicopedagogia.MyViewHolderPsicopedagogia> {
    Context contextPsico;
    ArrayList<EventsPsicopedagogia> arrayListPsico;
    DBOpenHelperPsicopedagogia dbOpenHelperPsico;

    public EventRecyclerAdapterPsicopedagogia(Context contextPsico, ArrayList<EventsPsicopedagogia> arrayList) {
        this.contextPsico = contextPsico;
        this.arrayListPsico = arrayList;
    }


    @NonNull
    @Override
    public EventRecyclerAdapterPsicopedagogia.MyViewHolderPsicopedagogia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout_psico, parent, false);

        return new EventRecyclerAdapterPsicopedagogia.MyViewHolderPsicopedagogia(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerAdapterPsicopedagogia.MyViewHolderPsicopedagogia holder, @SuppressLint("RecyclerView") int position) {

        EventsPsicopedagogia eventsPsico = arrayListPsico.get(position);
        holder.EventPsico.setText(eventsPsico.getEVENTPSICO());
        holder.DateTxtPsico.setText(eventsPsico.getDATEPSICO());
        holder.TimePsico.setText(eventsPsico.getTIMEPSICO());
        holder.deletePsico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCalendarEvent(eventsPsico.getEVENTPSICO(), eventsPsico.getDATEPSICO(), eventsPsico.getTIMEPSICO()); //LLAMADA AL MÉTODO, FALTA REFRESH PARA QUE SE ACTUALIZE LOS EVENTOS.
                arrayListPsico.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayListPsico.size();
    }

    public class MyViewHolderPsicopedagogia extends RecyclerView.ViewHolder{

        TextView DateTxtPsico, EventPsico, TimePsico;
        Button deletePsico;
        public MyViewHolderPsicopedagogia(@NonNull View itemView) {
            super(itemView);
            DateTxtPsico = itemView.findViewById(R.id.eventdatePsico);
            EventPsico = itemView.findViewById(R.id.eventnamePsico);
            TimePsico = itemView.findViewById(R.id.eventtimePsico);
            deletePsico = itemView.findViewById(R.id.deletePsico);


        }
    }

    private void deleteCalendarEvent(String event, String date, String time){
        DBOpenHelperPsicopedagogia dbOpenHelperPsico = new DBOpenHelperPsicopedagogia(contextPsico);
        SQLiteDatabase database = dbOpenHelperPsico.getWritableDatabase();
        dbOpenHelperPsico.deleteEvent(event, date, time, database);
        dbOpenHelperPsico.close();
    }
}
