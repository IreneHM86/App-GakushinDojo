package com.example.proyecto_final2.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.firestore.v1.StructuredQuery;

//DBOpenHelper hereda de la clase SQLiteOpenHelper que ayuda a la gestión de la creación de la base de datos
public class DBOpenHelper extends SQLiteOpenHelper {

    //Se crea la base de datos con su respectivos campos
    private static final String CREATE_EVENTS_TABLE = "create table " + DataBaseStructure.EVENT_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DataBaseStructure.EVENT + " TEXT, " + DataBaseStructure.TIME + " TEXT, " + DataBaseStructure.DATE + " TEXT, " + DataBaseStructure.MONTH + " TEXT, " +DataBaseStructure.YEAR+
            " TEXT, "+DataBaseStructure.Notify+" TEXT)";
    private static final String DROP_EVENTS_TABLE = "DROP TABLE IF EXISTS " + DataBaseStructure.EVENT_TABLE;


    public DBOpenHelper(@Nullable Context context) {
        super(context, DataBaseStructure.DB_NAME, null, DataBaseStructure.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE);

    }

    //modifica la tabla de datos eliminándola y creando una nueva
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EVENTS_TABLE);
        onCreate(db);
    }
    //guarda los dinstintos eventos creados
    public void SaveEvent(String event, String time, String date, String month, String year,String notify, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseStructure.EVENT, event);
        contentValues.put(DataBaseStructure.TIME, time);
        contentValues.put(DataBaseStructure.DATE, date);
        contentValues.put(DataBaseStructure.MONTH, month);
        contentValues.put(DataBaseStructure.YEAR, year);
        contentValues.put(DataBaseStructure.Notify, notify);
        database.insert(DataBaseStructure.EVENT_TABLE, null, contentValues);

    }
    //lee los eventos creados
    public Cursor ReadEvents(String date, SQLiteDatabase database) {
        String[] Projections = {DataBaseStructure.EVENT,DataBaseStructure.TIME,DataBaseStructure.DATE,DataBaseStructure.MONTH, DataBaseStructure.YEAR};
        String Selection = DataBaseStructure.DATE +"=?";
        String[] SelectionArgs = {date};

        return database.query(DataBaseStructure.EVENT_TABLE, Projections, Selection, SelectionArgs, null, null, null);
    }
    //lee el id de los eventos
    public Cursor ReadIDEvents(String date,String event, String time, SQLiteDatabase database) {
        String[] Projections = {DataBaseStructure.ID,DataBaseStructure.Notify};
        String Selection = DataBaseStructure.DATE +"=? and "+DataBaseStructure.EVENT+"=? and "+DataBaseStructure.TIME+"=?";
        String[] SelectionArgs = {date,event,time};

        return database.query(DataBaseStructure.EVENT_TABLE, Projections, Selection, SelectionArgs, null, null, null);
    }

    //lee los eventos de todo el mes
    public Cursor ReadEventsperMonth(String month, String year, SQLiteDatabase database) {
        String[] Projections = {DataBaseStructure.EVENT, DataBaseStructure.TIME, DataBaseStructure.DATE, DataBaseStructure.MONTH, DataBaseStructure.YEAR};
        String Selection = DataBaseStructure.MONTH +"=? and "+DataBaseStructure.YEAR+"=?";
        String[] SelectionArgs = {month,year};
        return database.query(DataBaseStructure.EVENT_TABLE, Projections, Selection, SelectionArgs, null, null, null);
    }
    //elimina los eventos
    public void deleteEvent(String event, String date, String time, SQLiteDatabase database) {
        String selection = DataBaseStructure.EVENT+"=? and "+DataBaseStructure.DATE+"=? and "+DataBaseStructure.TIME+"=?";
        String[] selectionArg = {event,date, time};
        database.delete(DataBaseStructure.EVENT_TABLE, selection,selectionArg);
    }
    //modifica los eventos
    public void updateEvent(String date,String event, String time, String notify, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseStructure.Notify, notify);
        String Selection = DataBaseStructure.DATE +"=? and "+DataBaseStructure.EVENT+"=? and "+DataBaseStructure.TIME+"=?";
        String[] SelectionArgs = {date,event,time};
        database.update(DataBaseStructure.EVENT_TABLE,contentValues,Selection,SelectionArgs );

    }
}
