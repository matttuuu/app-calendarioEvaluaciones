package com.example.calendarapp_idra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Activity4 extends AppCompatActivity {


    CustomCalendarViewIngles customCalendarViewIng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);
        customCalendarViewIng = (CustomCalendarViewIngles) findViewById(R.id.custom_calendar_view_ingles);
    }
}