package com.example.proyecto_final2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.proyecto_final2.Calendar.CustomCalendarView;

public class CalendarViewActivity extends AppCompatActivity {
    //Muestra el calendario
    CustomCalendarView customCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_calendar_layout);

        customCalendarView = (CustomCalendarView)findViewById(R.id.custom_calendar);

    }
}