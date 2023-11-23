package com.example.calendarapp_idra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Activity4 extends AppCompatActivity {

    CustomCalendarViewIngles customCalendarViewIngles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);
        customCalendarViewIngles = findViewById(R.id.custom_calendar_view_ingles);
    }
}