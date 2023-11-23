package com.example.calendarapp_idra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Activity3 extends AppCompatActivity {

    CustomCalendarViewTerapeutico customCalendarViewTera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        customCalendarViewTera = (CustomCalendarViewTerapeutico) findViewById(R.id.custom_calendar_view_terapeutico);

    }

}