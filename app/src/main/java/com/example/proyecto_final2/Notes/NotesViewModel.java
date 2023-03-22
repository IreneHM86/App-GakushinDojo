package com.example.proyecto_final2.Notes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.proyecto_final2.Notes.Notes;
import com.example.proyecto_final2.Notes.NotesRepository;

import java.util.List;

//permite almacenar y administrar datos relacionados con la interfaz
public class NotesViewModel extends AndroidViewModel {

    public NotesRepository repository;
    public LiveData<List<Notes>> getAllNotes;

    public NotesViewModel(Application application) {
        super(application);
        repository = new NotesRepository(application);
        getAllNotes = repository.getAllNotes;
    }

    public void insertNote(Notes notes){

        repository.insertNotes(notes);
    }
    public void deleteNote(int id){

        repository.deleteNotes(id);
    }
    public void updateNote(Notes notes){

        repository.updateNotes(notes);
    }
}
