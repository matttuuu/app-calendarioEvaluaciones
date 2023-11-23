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

public class EventRecyclerAdapterSeguridad extends RecyclerView.Adapter<EventRecyclerAdapterSeguridad.MyViewHolderSeguridad> {
    Context contextSeg;
    ArrayList<EventsSeguridad> arrayListSeg;
    DBOpenHelperSeguridad dbOpenHelperSeg;

    public EventRecyclerAdapterSeguridad(Context contextSeg, ArrayList<EventsSeguridad> arrayList) {
        this.contextSeg = contextSeg;
        this.arrayListSeg = arrayList;
    }


    @NonNull
    @Override
    public EventRecyclerAdapterSeguridad.MyViewHolderSeguridad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout_seg, parent, false);

        return new EventRecyclerAdapterSeguridad.MyViewHolderSeguridad(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerAdapterSeguridad.MyViewHolderSeguridad holder, @SuppressLint("RecyclerView") int position) {

        EventsSeguridad eventsSeg = arrayListSeg.get(position);
        holder.EventSeg.setText(eventsSeg.getEVENTSEG());
        holder.DateTxtSeg.setText(eventsSeg.getDATESEG());
        holder.TimeSeg.setText(eventsSeg.getTIMESEG());
        holder.deleteSeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCalendarEvent(eventsSeg.getEVENTSEG(), eventsSeg.getDATESEG(), eventsSeg.getTIMESEG()); //LLAMADA AL MÉTODO, FALTA REFRESH PARA QUE SE ACTUALIZE LOS EVENTOS.
                arrayListSeg.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayListSeg.size();
    }

    public class MyViewHolderSeguridad extends RecyclerView.ViewHolder{

        TextView DateTxtSeg, EventSeg, TimeSeg;
        Button deleteSeg;
        public MyViewHolderSeguridad(@NonNull View itemView) {
            super(itemView);
            DateTxtSeg = itemView.findViewById(R.id.eventdateSeg);
            EventSeg = itemView.findViewById(R.id.eventnameSeg);
            TimeSeg = itemView.findViewById(R.id.eventtimeSeg);
            deleteSeg = itemView.findViewById(R.id.deleteSeg);


        }
    }

    private void deleteCalendarEvent(String event, String date, String time){
        DBOpenHelperSeguridad dbOpenHelperSeg = new DBOpenHelperSeguridad(contextSeg);
        SQLiteDatabase database = dbOpenHelperSeg.getWritableDatabase();
        dbOpenHelperSeg.deleteEvent(event, date, time, database);
        dbOpenHelperSeg.close();
    }

}
