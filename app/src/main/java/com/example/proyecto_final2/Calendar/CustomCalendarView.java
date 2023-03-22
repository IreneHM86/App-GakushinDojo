package com.example.proyecto_final2.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_final2.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CustomCalendarView extends LinearLayout {

    //declaramos las variables
    ImageButton NextButton, PreviousButton;
    TextView CurrentDate;
    GridView gridView;
    //establecemos el número de días a 42
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    MyGridAdapter myGridAdapter;
    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();
    int alarmYear,alarmMonth,alarmDay,alarmHour,alarmMinute;


    DBOpenHelper dbOpenHelper;

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        InitializeLayout();
        SetUpCalendar();

        //botones que nos permite movernos por los distintos meses
        PreviousButton.setOnClickListener((view -> {
            calendar.add(Calendar.MONTH, -1);
            SetUpCalendar();
        }));

        NextButton.setOnClickListener((view -> {
            calendar.add(Calendar.MONTH, 1);
            SetUpCalendar();
        }));


        //Al pulsar en el calendario nos abre un cuadro de diálogo donde podremos introducir los siguientes elementos
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_event, null);
                EditText EventName = addView.findViewById(R.id.eventname);
                TextView EventTime = addView.findViewById(R.id.eventtime);
                ImageButton SetTime = addView.findViewById(R.id.settime);
                CheckBox alarmMe = addView.findViewById(R.id.alarmme);
                Calendar dateCalendar = Calendar.getInstance();
                dateCalendar.setTime(dates.get(position));
                //obtiene la fecha del día pulsado
                alarmYear = dateCalendar.get(Calendar.YEAR);
                alarmMonth = dateCalendar.get(Calendar.MONTH);
                alarmDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
                Button AddEvent = addView.findViewById(R.id.addevent);
                SetTime.setOnClickListener(new OnClickListener() {

                    //Seleccionamos una hora con TimePickerDialog
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minuts = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        Calendar c = Calendar.getInstance();
                                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        c.set(Calendar.MINUTE, minute);
                                        c.setTimeZone(TimeZone.getDefault());
                                        SimpleDateFormat hformate = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                        String event_Time = hformate.format(c.getTime());
                                        EventTime.setText(event_Time);
                                        alarmHour = c.get(Calendar.HOUR_OF_DAY);
                                        alarmMinute = c.get(Calendar.MINUTE);

                                    }

                                }, hours, minuts, false);
                        timePickerDialog.show();
                    }
                });
                final String date = eventDateFormate.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));
                AddEvent.setOnClickListener(new OnClickListener() {

                    //Comprueba si está seleccionado la casilla de la notificación
                    @Override
                    public void onClick(View view) {
                        if (alarmMe.isChecked()){
                            //si la casilla está en on, activa la alarma
                            SaveEvent(EventName.getText().toString(),EventTime.getText().toString(),date,month,year,"on");
                            SetUpCalendar();
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(alarmYear,alarmMonth,alarmDay,alarmHour,alarmMinute);
                            setAlarm(calendar,EventName.getText().toString(),EventTime.getText().toString(),getRequestCode(date,
                                    EventName.getText().toString(),EventTime.getText().toString()));
                            alertDialog.dismiss();
                            //si la casilla está en off, desactiva la alerta
                        }else{
                        SaveEvent(EventName.getText().toString(), EventTime.getText().toString(), date, month, year,"off");
                        SetUpCalendar();
                        alertDialog.dismiss();}
                    }
                });

                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        //Al mantener el click sobre un evento, se nos desplegará los eventos que tenga
        gridView.setOnItemLongClickListener((parent, view, position, id) -> {

            String date = eventDateFormate.format(dates.get(position));
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events, null);
            RecyclerView recyclerView = showView.findViewById(R.id.EventsRecView);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            EventRecAdapter eventRecAdapter = new EventRecAdapter(showView.getContext(), CollectEventByDate(date));
            recyclerView.setAdapter(eventRecAdapter);
            eventRecAdapter.notifyDataSetChanged();
            builder.setView(showView);
            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    SetUpCalendar();
                }
            });

            return true;

        });

    }
    //Devuelve la posición 0 de un índice dado por el nombre de las columnas
    private int getRequestCode(String date,String event,String time){
        int code = 0;
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadIDEvents(date,event, time,database);
        while (cursor.moveToNext()) {
           code = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseStructure.ID));
        }
        cursor.close();
        dbOpenHelper.close();
        return code;
    }
    //Método para activar la alarma dados unos parámetros (evento, hora y la id)
    private void setAlarm(Calendar calendar,String event, String time, int RequestCOde){
        Intent intent = new Intent(context.getApplicationContext(),AlarmRec.class);
        intent.putExtra("event",event);
        intent.putExtra("time",time);
        intent.putExtra("id",RequestCOde);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,RequestCOde,intent,PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager)context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

    }
  //Nos muestra un array con los diferentes eventos creados
    private ArrayList<Events> CollectEventByDate(String date) {
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);
        while (cursor.moveToNext()) {
            String event = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseStructure.TIME));
            String Date = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseStructure.MONTH));
            String Year = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseStructure.YEAR));
            Events events = new Events(event, time, Date, month, Year);
            arrayList.add(events);

        }
        cursor.close();
        dbOpenHelper.close();
        return arrayList;

    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    //guarda un evento creado
    private void SaveEvent(String event, String time, String date, String month, String year,String notify) {

        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, time, date, month, year,notify, database);
        dbOpenHelper.close();
        Toast.makeText(context, "Evento guardado", Toast.LENGTH_SHORT).show();

    }

    //Inicia el layout de la vista del calendario con sus correspondientes objetos View
    private void InitializeLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        NextButton = view.findViewById(R.id.nextBtn);
        PreviousButton = view.findViewById(R.id.previousBtn);
        CurrentDate = view.findViewById(R.id.currentDate);
        gridView = view.findViewById(R.id.gridview);
    }


    private void SetUpCalendar() {
        String currwntDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currwntDate);
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormat.format(calendar.getTime()));

        while (dates.size() < MAX_CALENDAR_DAYS) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        myGridAdapter = new MyGridAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(myGridAdapter);
    }
    //muestra los eventos por mes
    private void CollectEventsPerMonth(String Month, String year) {
        eventsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsperMonth(Month, year, database);
        while (cursor.moveToNext()) {
            String event = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseStructure.MONTH));
            String Year = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseStructure.YEAR));
            Events events = new Events(event, time, date, month, Year);
            eventsList.add(events);

        }
        cursor.close();
        dbOpenHelper.close();
    }


}
