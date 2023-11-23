package com.example.calendarapp_idra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

public class Activity7 extends AppCompatActivity {

    //SEGURIDAD Y HIGIENE.

    CustomCalendarViewSeguridad customCalendarViewSeg;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7);
        customCalendarViewSeg = (CustomCalendarViewSeguridad) findViewById(R.id.custom_calendar_view_seguridad);
    }
}