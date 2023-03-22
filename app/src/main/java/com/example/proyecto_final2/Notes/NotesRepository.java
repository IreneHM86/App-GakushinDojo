package com.example.proyecto_final2.Notes;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.proyecto_final2.Notes.NotesDao;
import com.example.proyecto_final2.Notes.NotesDatabase;
import com.example.proyecto_final2.Notes.Notes;

import java.util.List;

//Nos permite gestionar las operaciones de persistencia contra una tabla de la base de datos
public class NotesRepository {

    public NotesDao notesDao;
    public LiveData<List<Notes>> getAllNotes;

    //muestra las notas
    public NotesRepository(Application application) {

        NotesDatabase database = NotesDatabase.getDatabaseInstance(application);
        notesDao = database.notesDao();
        getAllNotes = notesDao.getallNotes();
    }
    //crea nuevas notas
    public void insertNotes(Notes notes) {
        notesDao.insertNotes(notes);
    }
    //elimina notas
    public void deleteNotes(int id) {
        notesDao.deleteNotes(id);
    }
    //modifica las notas
    public void updateNotes(Notes notes) {
        notesDao.updateNotes(notes);

    }

}
