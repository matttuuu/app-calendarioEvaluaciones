package com.example.calendarapp_idra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Activity6 extends AppCompatActivity {

    //Psicopedagogia.

    CustomCalendarViewPsicopedagogia customCalendarViewPsico;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6);
        customCalendarViewPsico = (CustomCalendarViewPsicopedagogia)findViewById(R.id.custom_calendar_view_psicopedagogia);
    }
}