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

public class EventRecyclerAdapterTerapeutico extends RecyclerView.Adapter<EventRecyclerAdapterTerapeutico.MyViewHolderTerapeutico> {
    Context contextTera;
    ArrayList<EventsTerapeutico> arrayListTera;
    DBOpenHelper dbOpenHelperTera;

    public EventRecyclerAdapterTerapeutico(Context contextTera, ArrayList<EventsTerapeutico> arrayList) {
        this.contextTera = contextTera;
        this.arrayListTera = arrayList;
    }


    @NonNull
    @Override
    public MyViewHolderTerapeutico onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout_tera, parent, false);

        return new EventRecyclerAdapterTerapeutico.MyViewHolderTerapeutico(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderTerapeutico holder, @SuppressLint("RecyclerView") int position) {

        EventsTerapeutico eventsTera = arrayListTera.get(position);
        holder.EventTera.setText(eventsTera.getEVENTTERA());
        holder.DateTxtTera.setText(eventsTera.getDATETERA());
        holder.TimeTera.setText(eventsTera.getTIMETERA());
        holder.deleteTera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCalendarEvent(eventsTera.getEVENTTERA(), eventsTera.getDATETERA(), eventsTera.getTIMETERA()); //LLAMADA AL MÉTODO, FALTA REFRESH PARA QUE SE ACTUALIZE LOS EVENTOS.
                arrayListTera.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayListTera.size();
    }

    public class MyViewHolderTerapeutico extends RecyclerView.ViewHolder{

        TextView DateTxtTera, EventTera, TimeTera;
        Button deleteTera;
        public MyViewHolderTerapeutico(@NonNull View itemView) {
            super(itemView);
            DateTxtTera = itemView.findViewById(R.id.eventdateTera);
            EventTera = itemView.findViewById(R.id.eventnameTera);
            TimeTera = itemView.findViewById(R.id.eventtimeTera);
            deleteTera = itemView.findViewById(R.id.deleteTera);


        }
    }

    private void deleteCalendarEvent(String event, String date, String time){
        dbOpenHelperTera = new DBOpenHelper(contextTera);
        SQLiteDatabase database = dbOpenHelperTera.getWritableDatabase();
        dbOpenHelperTera.deleteEvent(event, date, time, database);
        dbOpenHelperTera.close();
    }

}
