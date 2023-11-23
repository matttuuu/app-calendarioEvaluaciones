package com.example.calendarapp_idra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

public class Activity5 extends AppCompatActivity {

    //Educación Inicial.

    CustomCalendarViewEducacion customCalendarViewEdu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);
        customCalendarViewEdu = (CustomCalendarViewEducacion)findViewById(R.id.custom_calendar_view_educacion);

    }
}